package org.toolkit4j.quartz.task;

/** Thrown when a task id is not found. */
public class TaskNotFoundException extends RuntimeException {

  public TaskNotFoundException(String message) {
    super(message);
  }
}
