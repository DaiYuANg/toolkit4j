package org.toolkit4j.quartz.task;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.quartz.Job;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;

/**
 * Scheduled task metadata exposed by {@link TaskScheduler}.
 */
@RecordBuilder
public record TaskInfo(
  String taskId,
  Class<? extends Job> jobClass,
  String description,
  boolean durable,
  boolean requestRecovery,
  TaskScheduleKind scheduleType,
  String cronExpression,
  ZoneId cronZoneId,
  Duration interval,
  Instant startAt,
  Map<String, Object> jobData,
  Instant nextFireAt,
  boolean paused
) {}
