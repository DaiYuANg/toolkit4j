package org.toolkit4j.quartz.task;

import org.quartz.Job;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;

/**
 * Scheduled task metadata exposed by {@link TaskScheduler}.
 */
public record TaskInfo(
  String taskId,
  Class<? extends Job> jobClass,
  String description,
  boolean durable,
  boolean requestRecovery,
  boolean enabled,
  TaskScheduleKind scheduleType,
  String cronExpression,
  ZoneId cronZoneId,
  Duration interval,
  Instant startAt,
  Map<String, Object> jobData,
  Instant nextFireAt,
  boolean paused
) {}
