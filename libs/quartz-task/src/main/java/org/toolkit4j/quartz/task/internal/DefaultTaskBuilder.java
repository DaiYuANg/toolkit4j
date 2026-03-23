package org.toolkit4j.quartz.task.internal;

import org.toolkit4j.quartz.task.TaskBuilder;
import org.toolkit4j.quartz.task.TaskHandler;
import org.toolkit4j.quartz.task.TaskRegistrationException;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;

public class DefaultTaskBuilder implements TaskBuilder {
  private String id;
  private String description;

  private TaskSchedule schedule;
  private Object payload;
  private boolean enabled = true;

  @Override
  public TaskBuilder id(String id) {
    this.id = id;
    return this;
  }

  @Override
  public TaskBuilder description(String description) {
    this.description = description;
    return this;
  }

  @Override
  public TaskBuilder cron(String expression) {
    return cron(expression, ZoneId.systemDefault());
  }

  @Override
  public TaskBuilder cron(String expression, ZoneId zoneId) {
    this.schedule = TaskSchedule.cron(expression, zoneId);
    return this;
  }

  @Override
  public TaskBuilder once(Instant fireAt) {
    this.schedule = TaskSchedule.once(Objects.requireNonNull(fireAt, "fireAt"));
    return this;
  }

  @Override
  public TaskBuilder fixedInterval(Duration interval) {
    return fixedInterval(interval, Instant.now());
  }

  @Override
  public TaskBuilder fixedInterval(Duration interval, Instant startAt) {
    this.schedule = TaskSchedule.fixedInterval(interval, Objects.requireNonNull(startAt, "startAt"));
    return this;
  }

  @Override
  public TaskBuilder payload(Object payload) {
    this.payload = payload;
    return this;
  }

  @Override
  public TaskBuilder enabled(boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public TaskRegistration build(TaskHandler<?> handler) {
    if (id == null || id.isBlank()) {
      throw new TaskRegistrationException("task id is required.");
    }
    if (schedule == null) {
      throw new TaskRegistrationException("task schedule is required. Use cron/once/fixedInterval.");
    }

    Object p = payload;
    if (p != null) {
      if (!handler.payloadType().isInstance(p)) {
        throw new TaskRegistrationException(
          "payload type mismatch. expected=" + handler.payloadType().getName() + ", actual=" + p.getClass().getName()
        );
      }
    }

    return new TaskRegistration(
      id,
      handler.type(),
      description,
      enabled,
      p,
      schedule
    );
  }
}

