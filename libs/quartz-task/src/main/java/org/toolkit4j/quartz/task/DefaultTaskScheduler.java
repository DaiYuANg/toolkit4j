package org.toolkit4j.quartz.task;

import lombok.val;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.StdSchedulerFactory;
import org.toolkit4j.quartz.task.internal.DefaultTaskBuilder;
import org.toolkit4j.quartz.task.internal.TaskSchedule;
import org.toolkit4j.quartz.task.internal.TaskRegistration;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.function.Consumer;

/**
 * Default implementation of {@link TaskScheduler}.
 */
public class DefaultTaskScheduler implements TaskScheduler {

  private static final String JOB_GROUP = "toolkit4j-tasks";
  private static final String TRIGGER_GROUP = "toolkit4j-tasks";

  private final Scheduler scheduler;

  public DefaultTaskScheduler() {
    this(buildDefaultScheduler());
  }

  public DefaultTaskScheduler(Scheduler scheduler) {
    this.scheduler = Objects.requireNonNull(scheduler, "scheduler");
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
      val factory = new StdSchedulerFactory();
      return factory.getScheduler();
    } catch (SchedulerException e) {
      throw new TaskSchedulingException("failed to create Quartz scheduler", e);
    }
  }

  @Override
  public void register(Class<? extends Job> jobClass, Consumer<TaskOptions> options) {
    Objects.requireNonNull(jobClass, "jobClass");
    Objects.requireNonNull(options, "options");

    val builder = new DefaultTaskBuilder();
    options.accept(builder);
    val registration = builder.build(jobClass);

    val jobKey = jobKey(registration.taskId());
    JobBuilder jobBuilder = JobBuilder.newJob(registration.jobClass())
      .withIdentity(jobKey)
      .storeDurably(registration.durable())
      .requestRecovery(registration.requestRecovery());
    if (registration.description() != null && !registration.description().isBlank()) {
      jobBuilder = jobBuilder.withDescription(registration.description());
    }
    val jobDetail = jobBuilder
      .usingJobData(new JobDataMap(registration.jobData()))
      .build();

    val trigger = buildTrigger(jobKey, registration);

    try {
      scheduler.scheduleJob(jobDetail, trigger);
      if (!registration.enabled()) {
        scheduler.pauseJob(jobKey);
      }
    } catch (ObjectAlreadyExistsException e) {
      if (registration.conflictPolicy() == TaskRegistrationConflictPolicy.IGNORE_IF_EXISTS) {
        return;
      }
      if (registration.conflictPolicy() == TaskRegistrationConflictPolicy.RECREATE) {
        recreateExistingTask(registration, jobKey, jobDetail, trigger);
        return;
      }
      throw new TaskRegistrationException(
        "task id already exists: " + registration.taskId()
          + ". use ifExistsRecreate(true) to replace it or ifExistsIgnore(true) to skip.",
        e
      );
    } catch (SchedulerException e) {
      throw new TaskSchedulingException("failed to schedule job: " + registration.taskId(), e);
    }
  }

  private void recreateExistingTask(
    TaskRegistration registration,
    JobKey jobKey,
    JobDetail jobDetail,
    Trigger trigger
  ) {
    try {
      scheduler.deleteJob(jobKey);
      scheduler.scheduleJob(jobDetail, trigger);
      if (!registration.enabled()) {
        scheduler.pauseJob(jobKey);
      }
    } catch (SchedulerException e) {
      throw new TaskSchedulingException("failed to recreate task: " + registration.taskId(), e);
    }
  }

  @Override
  public void pause(String taskId) {
    requireNotNull(taskId);
    val jobKey = jobKey(taskId);
    withScheduler("pause task: " + taskId, () -> scheduler.pauseJob(jobKey));
    if (!exists(taskId)) {
      throw new TaskNotFoundException("task not found: " + taskId);
    }
  }

  @Override
  public void resume(String taskId) {
    requireNotNull(taskId);
    val jobKey = jobKey(taskId);
    withScheduler("resume task: " + taskId, () -> scheduler.resumeJob(jobKey));
    if (!exists(taskId)) {
      throw new TaskNotFoundException("task not found: " + taskId);
    }
  }

  @Override
  public void triggerNow(String taskId) {
    requireNotNull(taskId);
    val jobKey = jobKey(taskId);
    try {
      scheduler.triggerJob(jobKey);
    } catch (SchedulerException e) {
      if (!exists(taskId)) {
        throw new TaskNotFoundException("task not found: " + taskId);
      }
      throw new TaskSchedulingException("failed to trigger now: " + taskId, e);
    }
  }

  @Override
  public void unschedule(String taskId) {
    requireNotNull(taskId);
    val jobKey = jobKey(taskId);
    val deleted = withScheduler("unschedule task: " + taskId, () -> scheduler.deleteJob(jobKey));
    if (!deleted) {
      throw new TaskNotFoundException("task not found: " + taskId);
    }
  }

  @Override
  public boolean exists(String taskId) {
    requireNotNull(taskId);
    return withScheduler("check task existence: " + taskId, () -> scheduler.checkExists(jobKey(taskId)));
  }

  @Override
  public Optional<TaskInfo> getTask(String taskId) {
    requireNotNull(taskId);
    val jobKey = jobKey(taskId);
    val detail = withScheduler("query task: " + taskId, () -> scheduler.getJobDetail(jobKey));
    return Optional.ofNullable(detail).map(this::toTaskInfo);
  }

  @Override
  public List<TaskInfo> listTasks() {
    return withScheduler("list tasks", () -> scheduler.getJobKeys(GroupMatcher.jobGroupEquals(JOB_GROUP))).stream()
      .map(this::getJobDetail)
      .filter(Objects::nonNull)
      .map(this::toTaskInfo)
      .sorted(Comparator.comparing(TaskInfo::taskId))
      .toList();
  }

  private JobDetail getJobDetail(JobKey jobKey) {
    return withScheduler("query job detail: " + jobKey, () -> scheduler.getJobDetail(jobKey));
  }

  private void requireNotNull(String taskId) {
    if (taskId == null || taskId.isBlank()) {
      throw new IllegalArgumentException("taskId must not be blank.");
    }
  }

  private TaskInfo toTaskInfo(JobDetail jobDetail) {
    val triggers = getTriggers(jobDetail.getKey());
    val primaryTrigger = resolvePrimaryTrigger(triggers);
    val paused = isPaused(triggers, jobDetail.getKey().getName());
    val nextFireAt = nextFireAt(triggers);
    val metadata = scheduleMetadata(primaryTrigger);

    return new TaskInfo(
      jobDetail.getKey().getName(),
      jobDetail.getJobClass(),
      jobDetail.getDescription(),
      jobDetail.isDurable(),
      jobDetail.requestsRecovery(),
      !paused,
      metadata.kind(),
      metadata.cronExpression(),
      metadata.cronZoneId(),
      metadata.interval(),
      metadata.startAt(),
      jobDataOf(jobDetail.getJobDataMap()),
      nextFireAt,
      paused
    );
  }

  private List<? extends Trigger> getTriggers(JobKey jobKey) {
    return withScheduler("query triggers of: " + jobKey, () -> scheduler.getTriggersOfJob(jobKey));
  }

  private Map<String, Object> jobDataOf(JobDataMap jobDataMap) {
    return Map.copyOf(jobDataMap);
  }

  private Trigger resolvePrimaryTrigger(List<? extends Trigger> triggers) {
    return triggers.stream().min(Comparator.comparing(Trigger::getNextFireTime, Comparator.nullsLast(Date::compareTo)))
      .orElse(null);
  }

  private Instant nextFireAt(List<? extends Trigger> triggers) {
    return triggers.stream()
      .map(Trigger::getNextFireTime)
      .filter(Objects::nonNull)
      .map(Date::toInstant)
      .min(Comparator.naturalOrder())
      .orElse(null);
  }

  private boolean isPaused(List<? extends Trigger> triggers, String taskId) {
    if (triggers.isEmpty()) {
      return false;
    }
    return triggers.stream().allMatch(trigger -> {
      val state = withScheduler("query trigger state for: " + taskId, () -> scheduler.getTriggerState(trigger.getKey()));
      return state == Trigger.TriggerState.PAUSED;
    });
  }

  private Trigger buildTrigger(JobKey jobKey, TaskRegistration registration) {
    val schedule = registration.schedule();
    Objects.requireNonNull(schedule, "schedule");

    val identity = registration.taskId() + "-" + schedule.type().name().toLowerCase();

    switch (schedule.type()) {
      case CRON -> {
        return buildCronTrigger(jobKey, identity, schedule);
      }
      case ONCE -> {
        return buildOnceTrigger(jobKey, identity, schedule);
      }
      case FIXED_INTERVAL -> {
        return buildIntervalTrigger(jobKey, identity, schedule);
      }
      default -> throw new IllegalStateException("unknown schedule type: " + schedule.type());
    }
  }

  private Trigger buildCronTrigger(JobKey jobKey, String identity, TaskSchedule schedule) {
    val timezone = schedule.cronZoneId() == null ? TimeZone.getDefault() : TimeZone.getTimeZone(schedule.cronZoneId());
    final CronScheduleBuilder cronSchedule;
    try {
      cronSchedule = CronScheduleBuilder.cronSchedule(schedule.cronExpression()).inTimeZone(timezone);
    } catch (RuntimeException e) {
      throw new TaskRegistrationException("invalid cron expression: " + schedule.cronExpression(), e);
    }
    return TriggerBuilder.newTrigger()
      .withIdentity(identity, TRIGGER_GROUP)
      .forJob(jobKey)
      .withSchedule(cronSchedule)
      .build();
  }

  private Trigger buildOnceTrigger(JobKey jobKey, String identity, TaskSchedule schedule) {
    val startAt = Date.from(schedule.onceFireAt());
    return TriggerBuilder.newTrigger()
      .withIdentity(identity, TRIGGER_GROUP)
      .forJob(jobKey)
      .startAt(startAt)
      .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0))
      .build();
  }

  private Trigger buildIntervalTrigger(JobKey jobKey, String identity, TaskSchedule schedule) {
    val interval = schedule.fixedInterval();
    if (interval.isZero() || interval.isNegative()) {
      throw new TaskRegistrationException("fixedInterval must be positive.");
    }
    val startAt = Date.from(schedule.fixedStartAt());
    return TriggerBuilder.newTrigger()
      .withIdentity(identity, TRIGGER_GROUP)
      .forJob(jobKey)
      .startAt(startAt)
      .withSchedule(SimpleScheduleBuilder.simpleSchedule()
        .withIntervalInMilliseconds(interval.toMillis())
        .repeatForever())
      .build();
  }

  private ScheduleMetadata scheduleMetadata(Trigger primaryTrigger) {
    if (primaryTrigger == null) {
      return ScheduleMetadata.unknown();
    }
    val startAt = primaryTrigger.getStartTime() == null ? null : primaryTrigger.getStartTime().toInstant();
    if (primaryTrigger instanceof CronTrigger cronTrigger) {
      return new ScheduleMetadata(
        TaskScheduleKind.CRON,
        cronTrigger.getCronExpression(),
        cronTrigger.getTimeZone().toZoneId(),
        null,
        startAt
      );
    }
    if (primaryTrigger instanceof SimpleTrigger simpleTrigger) {
      if (simpleTrigger.getRepeatInterval() > 0L) {
        return new ScheduleMetadata(
          TaskScheduleKind.INTERVAL,
          null,
          null,
          Duration.ofMillis(simpleTrigger.getRepeatInterval()),
          startAt
        );
      }
      return new ScheduleMetadata(TaskScheduleKind.ONCE, null, null, null, startAt);
    }
    return new ScheduleMetadata(TaskScheduleKind.UNKNOWN, null, null, null, startAt);
  }

  private void withScheduler(String operation, QuartzRunnable runnable) {
    try {
      runnable.run();
    } catch (SchedulerException e) {
      throw new TaskSchedulingException("failed to " + operation, e);
    }
  }

  private <T> T withScheduler(String operation, QuartzSupplier<T> supplier) {
    try {
      return supplier.get();
    } catch (SchedulerException e) {
      throw new TaskSchedulingException("failed to " + operation, e);
    }
  }

  @FunctionalInterface
  private interface QuartzRunnable {
    void run() throws SchedulerException;
  }

  @FunctionalInterface
  private interface QuartzSupplier<T> {
    T get() throws SchedulerException;
  }

  private record ScheduleMetadata(
    TaskScheduleKind kind,
    String cronExpression,
    ZoneId cronZoneId,
    Duration interval,
    Instant startAt
  ) {
    private static ScheduleMetadata unknown() {
      return new ScheduleMetadata(TaskScheduleKind.UNKNOWN, null, null, null, null);
    }
  }

  private JobKey jobKey(String taskId) {
    return JobKey.jobKey(taskId, JOB_GROUP);
  }
}

