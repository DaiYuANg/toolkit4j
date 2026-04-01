package org.toolkit4j.quartz.task.internal;

import io.soabase.recordbuilder.core.RecordBuilder;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;

@RecordBuilder
public record TaskSchedule(
    TaskScheduleType type,
    String cronExpression,
    ZoneId cronZoneId,
    Instant onceFireAt,
    Duration fixedInterval,
    Instant fixedStartAt) {
  public static TaskSchedule cron(String expression, ZoneId zoneId) {
    Objects.requireNonNull(expression, "expression");
    ZoneId resolvedZoneId = zoneId == null ? ZoneId.systemDefault() : zoneId;
    return TaskScheduleBuilder.builder()
        .type(TaskScheduleType.CRON)
        .cronExpression(expression)
        .cronZoneId(resolvedZoneId)
        .build();
  }

  public static TaskSchedule once(Instant fireAt) {
    Objects.requireNonNull(fireAt, "fireAt");
    return TaskScheduleBuilder.builder().type(TaskScheduleType.ONCE).onceFireAt(fireAt).build();
  }

  public static TaskSchedule fixedInterval(Duration interval, Instant startAt) {
    Objects.requireNonNull(interval, "interval");
    Objects.requireNonNull(startAt, "startAt");
    if (interval.isZero() || interval.isNegative()) {
      throw new IllegalArgumentException("interval must be positive.");
    }
    return TaskScheduleBuilder.builder()
        .type(TaskScheduleType.FIXED_INTERVAL)
        .fixedInterval(interval)
        .fixedStartAt(startAt)
        .build();
  }
}
