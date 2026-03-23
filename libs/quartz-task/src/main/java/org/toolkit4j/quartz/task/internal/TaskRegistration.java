package org.toolkit4j.quartz.task.internal;

public record TaskRegistration(
  String taskId,
  String taskType,
  String description,
  boolean enabled,
  Object payload,
  TaskSchedule schedule
) {}

