package org.toolkit4j.quartz.task;

/**
 * How a task execution was triggered.
 */
public enum TriggerSource {
  SCHEDULER,
  MANUAL,
  RECOVERY
}
