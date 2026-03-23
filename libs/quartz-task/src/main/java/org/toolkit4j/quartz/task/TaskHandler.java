package org.toolkit4j.quartz.task;

/**
 * Main extension point for task business logic.
 * Implementations should be stateless or thread-safe.
 */
public interface TaskHandler<T> {

  /**
   * Unique task type identifier.
   */
  String type();

  /**
   * Payload type for this handler.
   */
  Class<T> payloadType();

  /**
   * Execute task logic.
   *
   * @param context execution context
   */
  void execute(TaskContext<T> context) throws Exception;
}
