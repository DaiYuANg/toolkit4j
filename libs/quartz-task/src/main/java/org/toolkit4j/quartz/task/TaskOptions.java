package org.toolkit4j.quartz.task;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;

/** Builder-style options for task registration. */
public interface TaskOptions {

  TaskOptions id(String id);

  TaskOptions description(String description);

  TaskOptions cron(String expression);

  TaskOptions cron(String expression, ZoneId zoneId);

  TaskOptions startAt(Instant startAt);

  TaskOptions interval(Duration interval);

  TaskOptions interval(Duration interval, Instant startAt);

  TaskOptions jobData(String key, String value);

  TaskOptions jobData(String key, int value);

  TaskOptions jobData(String key, long value);

  TaskOptions jobData(String key, boolean value);

  TaskOptions jobData(Map<String, ?> values);

  TaskOptions durable(boolean durable);

  TaskOptions requestRecovery(boolean requestRecovery);

  TaskOptions registrationConflictPolicy(TaskRegistrationConflictPolicy policy);

  TaskOptions ifExistsRecreate(boolean enabled);

  /**
   * When {@code true}, an existing task id is left unchanged (no error). When {@code false},
   * restores default {@link TaskRegistrationConflictPolicy#FAIL}.
   */
  TaskOptions ifExistsIgnore(boolean enabled);
}
