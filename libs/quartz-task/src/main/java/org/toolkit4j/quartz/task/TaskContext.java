package org.toolkit4j.quartz.task;

/**
 * Minimal execution context for a task.
 */
public interface TaskContext<T> {

  String taskId();

  String taskType();

  T payload();
}
