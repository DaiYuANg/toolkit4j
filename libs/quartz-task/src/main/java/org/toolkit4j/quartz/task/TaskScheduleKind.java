package org.toolkit4j.quartz.task;

/**
 * High-level schedule type exposed by task metadata.
 */
public enum TaskScheduleKind {
  CRON,
  ONCE,
  INTERVAL,
  UNKNOWN
}
