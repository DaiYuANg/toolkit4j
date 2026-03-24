package org.toolkit4j.quartz.task.internal;

import lombok.val;
import org.quartz.Job;
import org.toolkit4j.quartz.task.TaskOptions;
import org.toolkit4j.quartz.task.TaskRegistrationConflictPolicy;
import org.toolkit4j.quartz.task.TaskRegistrationException;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultTaskBuilder implements TaskOptions {
  private String id;
  private String description;

  private TaskSchedule schedule;
  private final Map<String, Object> jobData = new LinkedHashMap<>();
  private boolean durable;
  private boolean requestRecovery;
  private boolean enabled = true;
  private TaskRegistrationConflictPolicy conflictPolicy = TaskRegistrationConflictPolicy.FAIL;

  @Override
  public TaskOptions id(String id) {
    this.id = id;
    return this;
  }

  @Override
  public TaskOptions description(String description) {
    this.description = description;
    return this;
  }

  @Override
  public TaskOptions cron(String expression) {
    return cron(expression, ZoneId.systemDefault());
  }

  @Override
  public TaskOptions cron(String expression, ZoneId zoneId) {
    this.schedule = TaskSchedule.cron(expression, zoneId);
    return this;
  }

  @Override
  public TaskOptions startAt(Instant startAt) {
    this.schedule = TaskSchedule.once(Objects.requireNonNull(startAt, "startAt"));
    return this;
  }

  @Override
  public TaskOptions interval(Duration interval) {
    return interval(interval, Instant.now());
  }

  @Override
  public TaskOptions interval(Duration interval, Instant startAt) {
    this.schedule = TaskSchedule.fixedInterval(interval, Objects.requireNonNull(startAt, "startAt"));
    return this;
  }

  @Override
  public TaskOptions jobData(String key, String value) {
    putJobData(key, value);
    return this;
  }

  @Override
  public TaskOptions jobData(String key, int value) {
    putJobData(key, value);
    return this;
  }

  @Override
  public TaskOptions jobData(String key, long value) {
    putJobData(key, value);
    return this;
  }

  @Override
  public TaskOptions jobData(String key, boolean value) {
    putJobData(key, value);
    return this;
  }

  @Override
  public TaskOptions jobData(Map<String, ?> values) {
    Objects.requireNonNull(values, "values");
    values.forEach(this::putJobData);
    return this;
  }

  @Override
  public TaskOptions durable(boolean durable) {
    this.durable = durable;
    return this;
  }

  @Override
  public TaskOptions requestRecovery(boolean requestRecovery) {
    this.requestRecovery = requestRecovery;
    return this;
  }

  @Override
  public TaskOptions enabled(boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  @Override
  public TaskOptions registrationConflictPolicy(TaskRegistrationConflictPolicy policy) {
    this.conflictPolicy = Objects.requireNonNull(policy, "policy");
    return this;
  }

  @Override
  public TaskOptions ifExistsRecreate(boolean enabled) {
    this.conflictPolicy = enabled ? TaskRegistrationConflictPolicy.RECREATE : TaskRegistrationConflictPolicy.FAIL;
    return this;
  }

  @Override
  public TaskOptions ifExistsIgnore(boolean enabled) {
    this.conflictPolicy = enabled ? TaskRegistrationConflictPolicy.IGNORE_IF_EXISTS : TaskRegistrationConflictPolicy.FAIL;
    return this;
  }

  public TaskRegistration build(Class<? extends Job> jobClass) {
    Objects.requireNonNull(jobClass, "jobClass");

    if (id == null || id.isBlank()) {
      throw new TaskRegistrationException("task id is required.");
    }
    if (schedule == null) {
      throw new TaskRegistrationException("task schedule is required. Use cron/startAt/interval.");
    }

    val copiedJobData = Map.copyOf(jobData);

    return new TaskRegistration(
      id,
      jobClass,
      description,
      durable,
      requestRecovery,
      enabled,
      conflictPolicy,
      copiedJobData,
      schedule
    );
  }

  private void putJobData(String key, Object value) {
    if (key == null || key.isBlank()) {
      throw new TaskRegistrationException("jobData key must not be blank.");
    }
    jobData.put(key, value);
  }
}

