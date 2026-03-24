package org.toolkit4j.quartz.task;

/**
 * Conflict policy used when registering a task with an existing id.
 */
public enum TaskRegistrationConflictPolicy {
  /** Fail with {@link org.toolkit4j.quartz.task.TaskRegistrationException}. */
  FAIL,
  /** Delete the existing job and schedule the new registration. */
  RECREATE,
  /** If the job already exists, do nothing and leave the existing schedule as-is (idempotent registration). */
  IGNORE_IF_EXISTS
}
