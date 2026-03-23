package org.toolkit4j.quartz.task;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.toolkit4j.quartz.task.internal.DefaultTaskBuilder;
import org.toolkit4j.quartz.task.internal.DefaultTaskContext;
import org.toolkit4j.quartz.task.internal.QuartzBridgeJob;
import org.toolkit4j.quartz.task.internal.TaskSchedule;
import org.toolkit4j.quartz.task.internal.TaskScheduleType;
import org.toolkit4j.quartz.task.internal.TaskStateManager;
import org.toolkit4j.quartz.task.internal.TaskRegistration;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Default implementation of {@link TaskScheduler}.
 *
 * <p>All runtime status and recent execution history are stored in-memory.
 */
public class DefaultTaskScheduler implements TaskScheduler {

  private static final String TRIGGER_GROUP = "toolkit4j-tasks";

  private final Scheduler scheduler;
  private final TaskStateManager stateManager;

  public DefaultTaskScheduler() {
    this(buildDefaultScheduler());
  }

  public DefaultTaskScheduler(Scheduler scheduler) {
    this.scheduler = Objects.requireNonNull(scheduler, "scheduler");
    this.stateManager = new TaskStateManager();
    try {
      this.scheduler.getContext().put(TaskStateManager.QUARTZ_CONTEXT_KEY, stateManager);
    } catch (SchedulerException e) {
      throw new TaskSchedulingException("failed to put TaskStateManager into Quartz context", e);
    }
    try {
      if (!this.scheduler.isStarted()) {
        this.scheduler.start();
      }
    } catch (SchedulerException e) {
      throw new TaskSchedulingException("failed to start Quartz scheduler", e);
    }
  }

  private static Scheduler buildDefaultScheduler() {
    try {
      var factory = new StdSchedulerFactory();
      Scheduler s = factory.getScheduler();
      return s;
    } catch (SchedulerException e) {
      throw new TaskSchedulingException("failed to create Quartz scheduler", e);
    }
  }

  @Override
  public void register(TaskHandler<?> handler, Consumer<TaskBuilder> options) {
    Objects.requireNonNull(handler, "handler");
    Objects.requireNonNull(options, "options");

    DefaultTaskBuilder builder = new DefaultTaskBuilder();
    options.accept(builder);
    TaskRegistration registration = builder.build(handler);

    // Register runtime state first (so job execution can resolve immediately).
    stateManager.register(handler, registration);

    JobKey jobKey = JobKey.jobKey(registration.taskId());

    JobDetail jobDetail = JobBuilder.newJob(QuartzBridgeJob.class)
      .withIdentity(jobKey)
      .usingJobData(TaskStateManager.JOB_DATA_TASK_ID, registration.taskId())
      .build();

    Trigger trigger = buildTrigger(jobKey, registration, TriggerSource.SCHEDULER);

    try {
      scheduler.scheduleJob(jobDetail, trigger);
      if (!registration.enabled()) {
        scheduler.pauseJob(jobKey);
      }
    } catch (ObjectAlreadyExistsException e) {
      // Roll back runtime state (best-effort).
      stateManager.unregister(registration.taskId());
      throw new TaskRegistrationException("task id already exists: " + registration.taskId(), e);
    } catch (SchedulerException e) {
      stateManager.unregister(registration.taskId());
      throw new TaskSchedulingException("failed to schedule job: " + registration.taskId(), e);
    }
  }

  @Override
  public void pause(String taskId) {
    requireTask(taskId);
    JobKey jobKey = JobKey.jobKey(taskId);
    try {
      scheduler.pauseJob(jobKey);
    } catch (SchedulerException e) {
      throw new TaskSchedulingException("failed to pause task: " + taskId, e);
    }
  }

  @Override
  public void resume(String taskId) {
    requireTask(taskId);
    JobKey jobKey = JobKey.jobKey(taskId);
    try {
      scheduler.resumeJob(jobKey);
    } catch (SchedulerException e) {
      throw new TaskSchedulingException("failed to resume task: " + taskId, e);
    }
  }

  @Override
  public void triggerNow(String taskId) {
    requireTask(taskId);
    JobKey jobKey = JobKey.jobKey(taskId);
    try {
      Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity(taskId + "-manual-" + UUID.randomUUID(), TRIGGER_GROUP)
        .forJob(jobKey)
        .startNow()
        .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0))
        .usingJobData(TaskStateManager.JOB_DATA_TRIGGER_SOURCE, TriggerSource.MANUAL.name())
        .build();
      scheduler.scheduleJob(trigger);
    } catch (SchedulerException e) {
      throw new TaskSchedulingException("failed to trigger now: " + taskId, e);
    }
  }

  @Override
  public void unschedule(String taskId) {
    requireTask(taskId);
    JobKey jobKey = JobKey.jobKey(taskId);
    stateManager.unregister(taskId);
    try {
      scheduler.deleteJob(jobKey);
    } catch (SchedulerException e) {
      throw new TaskSchedulingException("failed to unschedule task: " + taskId, e);
    }
  }

  @Override
  public Optional<TaskStatus> getStatus(String taskId) {
    requireNotNull(taskId);
    Instant nextFireAt = nextFireAt(taskId);
    return stateManager.getStatus(taskId, nextFireAt);
  }

  @Override
  public List<TaskStatus> listStatuses() {
    List<TaskStatus> list = new ArrayList<>();
    for (String taskId : stateManager.taskIds()) {
      Instant next = nextFireAt(taskId);
      stateManager.getStatus(taskId, next).ifPresent(list::add);
    }
    return list;
  }

  @Override
  public List<TaskExecutionRecord> getRecentExecutions(String taskId, int limit) {
    requireNotNull(taskId);
    return stateManager.recentExecutions(taskId, limit);
  }

  private void requireTask(String taskId) {
    requireNotNull(taskId);
    if (!stateManager.containsTask(taskId)) {
      throw new TaskNotFoundException("task not found: " + taskId);
    }
  }

  private void requireNotNull(String taskId) {
    if (taskId == null || taskId.isBlank()) {
      throw new IllegalArgumentException("taskId must not be blank.");
    }
  }

  private Instant nextFireAt(String taskId) {
    JobKey jobKey = JobKey.jobKey(taskId);
    try {
      List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
      return triggers.stream()
        .map(Trigger::getNextFireTime)
        .filter(Objects::nonNull)
        .map(Date::toInstant)
        .min(Comparator.naturalOrder())
        .orElse(null);
    } catch (SchedulerException e) {
      throw new TaskSchedulingException("failed to query nextFireAt for: " + taskId, e);
    }
  }

  private Trigger buildTrigger(JobKey jobKey, TaskRegistration registration, TriggerSource source) {
    TaskSchedule schedule = registration.schedule();
    Objects.requireNonNull(schedule, "schedule");

    String identity = registration.taskId() + "-" + schedule.type().name().toLowerCase();

    switch (schedule.type()) {
      case CRON -> {
        TimeZone tz = schedule.cronZoneId() == null ? TimeZone.getDefault() : TimeZone.getTimeZone(schedule.cronZoneId());
        CronScheduleBuilder cronSchedule = CronScheduleBuilder.cronSchedule(schedule.cronExpression()).inTimeZone(tz);
        return TriggerBuilder.newTrigger()
          .withIdentity(identity, TRIGGER_GROUP)
          .forJob(jobKey)
          .withSchedule(cronSchedule)
          .usingJobData(TaskStateManager.JOB_DATA_TRIGGER_SOURCE, source.name())
          .build();
      }
      case ONCE -> {
        Date startAt = Date.from(schedule.onceFireAt());
        return TriggerBuilder.newTrigger()
          .withIdentity(identity, TRIGGER_GROUP)
          .forJob(jobKey)
          .startAt(startAt)
          .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0))
          .usingJobData(TaskStateManager.JOB_DATA_TRIGGER_SOURCE, source.name())
          .build();
      }
      case FIXED_INTERVAL -> {
        Duration interval = schedule.fixedInterval();
        if (interval.isZero() || interval.isNegative()) {
          throw new TaskRegistrationException("fixedInterval must be positive.");
        }
        Date startAt = Date.from(schedule.fixedStartAt());
        return TriggerBuilder.newTrigger()
          .withIdentity(identity, TRIGGER_GROUP)
          .forJob(jobKey)
          .startAt(startAt)
          .withSchedule(SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInMilliseconds(interval.toMillis())
            .repeatForever())
          .usingJobData(TaskStateManager.JOB_DATA_TRIGGER_SOURCE, source.name())
          .build();
      }
      default -> throw new IllegalStateException("unknown schedule type: " + schedule.type());
    }
  }
}

