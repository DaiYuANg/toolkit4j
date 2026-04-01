package org.toolkit4j.quartz.task;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.quartz.Job;

/** Main entry point for task scheduling. */
public interface TaskScheduler {

  void register(Class<? extends Job> jobClass, Consumer<TaskOptions> options);

  void pause(String taskId);

  void resume(String taskId);

  void triggerNow(String taskId);

  void unschedule(String taskId);

  boolean exists(String taskId);

  Optional<TaskInfo> getTask(String taskId);

  List<TaskInfo> listTasks();
}
