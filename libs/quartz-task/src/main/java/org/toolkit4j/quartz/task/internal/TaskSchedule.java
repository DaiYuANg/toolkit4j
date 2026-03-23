package org.toolkit4j.quartz.task.internal;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;

public record TaskSchedule(
  TaskScheduleType type,
  String cronExpression,
  ZoneId cronZoneId,
  Instant onceFireAt,
  Duration fixedInterval,
  Instant fixedStartAt
) {
  public static TaskSchedule cron(String expression, ZoneId zoneId) {
    Objects.requireNonNull(expression, "expression");
    ZoneId z = zoneId == null ? ZoneId.systemDefault() : zoneId;
    return new TaskSchedule(TaskScheduleType.CRON, expression, z, null, null, null);
  }

  public static TaskSchedule once(Instant fireAt) {
    Objects.requireNonNull(fireAt, "fireAt");
    return new TaskSchedule(TaskScheduleType.ONCE, null, null, fireAt, null, null);
  }

  public static TaskSchedule fixedInterval(Duration interval, Instant startAt) {
    Objects.requireNonNull(interval, "interval");
    Objects.requireNonNull(startAt, "startAt");
    if (interval.isZero() || interval.isNegative()) {
      throw new IllegalArgumentException("interval must be positive.");
    }
    return new TaskSchedule(TaskScheduleType.FIXED_INTERVAL, null, null, null, interval, startAt);
  }
}

