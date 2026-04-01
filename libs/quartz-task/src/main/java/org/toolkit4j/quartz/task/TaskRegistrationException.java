package org.toolkit4j.quartz.task;

/** Thrown when task registration fails (e.g. duplicate id, invalid cron). */
public class TaskRegistrationException extends RuntimeException {

  public TaskRegistrationException(String message) {
    super(message);
  }

  public TaskRegistrationException(String message, Throwable cause) {
    super(message, cause);
  }
}
