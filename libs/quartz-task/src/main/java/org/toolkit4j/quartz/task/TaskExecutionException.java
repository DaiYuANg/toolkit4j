package org.toolkit4j.quartz.task;

/**
 * Wraps exceptions thrown during task execution.
 */
public class TaskExecutionException extends RuntimeException {

  public TaskExecutionException(String message, Throwable cause) {
    super(message, cause);
  }
}
