package org.toolkit4j.quartz.task;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

@RecordBuilder
record ScheduleMetadata(
  TaskScheduleKind kind,
  String cronExpression,
  ZoneId cronZoneId,
  Duration interval,
  Instant startAt
) {
  static ScheduleMetadata unknown() {
    return ScheduleMetadataBuilder.builder()
      .kind(TaskScheduleKind.UNKNOWN)
      .build();
  }
}
