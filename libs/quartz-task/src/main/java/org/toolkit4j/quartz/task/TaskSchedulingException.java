package org.toolkit4j.quartz.task;

/**
 * Thrown when scheduling operations fail.
 */
public class TaskSchedulingException extends RuntimeException {

  public TaskSchedulingException(String message) {
    super(message);
  }

  public TaskSchedulingException(String message, Throwable cause) {
    super(message, cause);
  }
}
