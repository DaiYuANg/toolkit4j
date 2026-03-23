package org.toolkit4j.quartz.task;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Main entry point for task scheduling.
 */
public interface TaskScheduler {

  void register(TaskHandler<?> handler, Consumer<TaskBuilder> options);

  void pause(String taskId);

  void resume(String taskId);

  void triggerNow(String taskId);

  void unschedule(String taskId);

  Optional<TaskStatus> getStatus(String taskId);

  List<TaskStatus> listStatuses();

  List<TaskExecutionRecord> getRecentExecutions(String taskId, int limit);
}
