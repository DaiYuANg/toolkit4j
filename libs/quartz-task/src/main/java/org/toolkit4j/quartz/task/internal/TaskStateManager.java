package org.toolkit4j.quartz.task.internal;

import org.toolkit4j.quartz.task.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TaskStateManager {

  public static final String QUARTZ_CONTEXT_KEY = TaskStateManager.class.getName();

  public static final String JOB_DATA_TASK_ID = "taskId";
  public static final String JOB_DATA_TRIGGER_SOURCE = "triggerSource";

  private static final int DEFAULT_MAX_RECENT_EXECUTIONS = 100;

  private final int maxRecentExecutions;

  private final ConcurrentMap<String, RegisteredTask> tasksById = new ConcurrentHashMap<>();
  private final ConcurrentMap<String, TaskHandler<?>> handlersByType = new ConcurrentHashMap<>();

  public TaskStateManager() {
    this(DEFAULT_MAX_RECENT_EXECUTIONS);
  }

  public TaskStateManager(int maxRecentExecutions) {
    this.maxRecentExecutions = maxRecentExecutions;
  }

  public void register(TaskHandler<?> handler, TaskRegistration registration) {
    Objects.requireNonNull(handler, "handler");
    Objects.requireNonNull(registration, "registration");

    // Enforce handler type uniqueness (by handler.type()).
    TaskHandler<?> existing = handlersByType.putIfAbsent(handler.type(), handler);
    if (existing != null && existing != handler) {
      throw new TaskRegistrationException("handler type already registered: " + handler.type());
    }

    RegisteredTask previous = tasksById.putIfAbsent(registration.taskId(), new RegisteredTask(
      registration.taskId(),
      registration.taskType(),
      registration.description(),
      registration.enabled(),
      handler,
      registration.payload()
    ));

    if (previous != null) {
      throw new TaskRegistrationException("task id already registered: " + registration.taskId());
    }
  }

  public void unregister(String taskId) {
    tasksById.remove(taskId);
  }

  public boolean containsTask(String taskId) {
    return tasksById.containsKey(taskId);
  }

  public Set<String> taskIds() {
    return tasksById.keySet();
  }

  public Optional<TaskStatus> getStatus(String taskId, Instant nextFireAt) {
    RegisteredTask task = tasksById.get(taskId);
    if (task == null) return Optional.empty();
    return Optional.of(task.snapshot(nextFireAt));
  }

  public List<TaskStatus> listStatuses(InstantSupplier nextFireAtSupplier) {
    // nextFireAtSupplier is provided by the outer scheduler to avoid Quartz calls from this class.
    List<TaskStatus> statuses = new ArrayList<>();
    for (String taskId : tasksById.keySet()) {
      RegisteredTask task = tasksById.get(taskId);
      if (task == null) continue;
      statuses.add(task.snapshot(nextFireAtSupplier.get(taskId)));
    }
    return statuses;
  }

  public List<TaskExecutionRecord> recentExecutions(String taskId, int limit) {
    if (limit <= 0) return List.of();
    RegisteredTask task = tasksById.get(taskId);
    if (task == null) return List.of();

    List<TaskExecutionRecord> list;
    synchronized (task.lock) {
      list = new ArrayList<>(task.recent);
    }

    list.sort(Comparator.comparing(TaskExecutionRecord::finishedAt).reversed());
    if (list.size() > limit) {
      list = list.subList(0, limit);
    }
    return list;
  }

  public void execute(String taskId, TriggerSource triggerSource) {
    RegisteredTask task = tasksById.get(taskId);
    if (task == null) {
      throw new TaskNotFoundException("task not found: " + taskId);
    }

    String executionId = UUID.randomUUID().toString();
    Instant startedAt = Instant.now();

    synchronized (task.lock) {
      task.running = true;
      task.lastStartedAt = startedAt;
      task.lastStatus = ExecutionStatus.RUNNING;
      task.lastErrorMessage = null;
      task.lastErrorStackTrace = null;
      task.lastDurationMs = null;
    }

    ExecutionStatus finalStatus;
    String errorMessage = null;
    String errorStackTrace = null;

    try {
      @SuppressWarnings("unchecked")
      TaskHandler<Object> handler = (TaskHandler<Object>) task.handler;

      @SuppressWarnings("unchecked")
      Object payload = task.payload;

      TaskContext<Object> context = new DefaultTaskContext<>(task.taskId, task.taskType, payload);
      handler.execute(context);
      finalStatus = ExecutionStatus.SUCCESS;
    } catch (Throwable t) {
      finalStatus = ExecutionStatus.FAILED;
      errorMessage = t.getMessage();
      errorStackTrace = toStackTrace(t);
    }

    Instant finishedAt = Instant.now();
    long durationMs = Duration.between(startedAt, finishedAt).toMillis();

    synchronized (task.lock) {
      task.running = false;
      task.lastFinishedAt = finishedAt;
      task.lastDurationMs = durationMs;
      task.lastStatus = finalStatus;

      if (finalStatus == ExecutionStatus.SUCCESS) {
        task.lastSuccessAt = finishedAt;
        task.consecutiveFailureCount = 0;
      } else {
        task.lastFailureAt = finishedAt;
        task.consecutiveFailureCount = (task.consecutiveFailureCount == null) ? 1 : task.consecutiveFailureCount + 1;
        task.lastErrorMessage = errorMessage;
        task.lastErrorStackTrace = errorStackTrace;
      }

      TaskExecutionRecord record = new TaskExecutionRecord(
        executionId,
        task.taskId,
        task.taskType,
        startedAt,
        finishedAt,
        durationMs,
        finalStatus,
        errorMessage,
        errorStackTrace,
        triggerSource
      );

      task.recent.addLast(record);
      while (task.recent.size() > maxRecentExecutions) {
        task.recent.removeFirst();
      }
    }
  }

  private static String toStackTrace(Throwable t) {
    StringWriter sw = new StringWriter();
    try (PrintWriter pw = new PrintWriter(sw)) {
      t.printStackTrace(pw);
    }
    return sw.toString();
  }

  @FunctionalInterface
  public interface InstantSupplier {
    Instant get(String taskId);
  }

  private static final class RegisteredTask {
    private final Object lock = new Object();
    private final String taskId;
    private final String taskType;
    @SuppressWarnings("unused")
    private final String description;
    private final boolean enabled;
    private final TaskHandler<?> handler;
    private final Object payload;

    private volatile boolean running;
    private Instant lastStartedAt;
    private Instant lastFinishedAt;
    private Instant lastSuccessAt;
    private Instant lastFailureAt;
    private ExecutionStatus lastStatus;
    private String lastErrorMessage;
    private String lastErrorStackTrace;
    private Long lastDurationMs;
    private Integer consecutiveFailureCount;

    private final Deque<TaskExecutionRecord> recent;

    private RegisteredTask(
      String taskId,
      String taskType,
      String description,
      boolean enabled,
      TaskHandler<?> handler,
      Object payload
    ) {
      this.taskId = taskId;
      this.taskType = taskType;
      this.description = description;
      this.enabled = enabled;
      this.handler = handler;
      this.payload = payload;
      this.recent = new ArrayDeque<>();
      this.consecutiveFailureCount = 0;
    }

    private TaskStatus snapshot(Instant nextFireAt) {
      synchronized (lock) {
        return new TaskStatus(
          taskId,
          taskType,
          enabled,
          running,
          lastStartedAt,
          lastFinishedAt,
          lastSuccessAt,
          lastFailureAt,
          nextFireAt,
          lastStatus,
          lastErrorMessage,
          lastErrorStackTrace,
          lastDurationMs,
          consecutiveFailureCount
        );
      }
    }
  }
}

