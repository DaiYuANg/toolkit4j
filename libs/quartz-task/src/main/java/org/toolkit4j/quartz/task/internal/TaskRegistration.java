package org.toolkit4j.quartz.task.internal;

import org.quartz.Job;
import org.toolkit4j.quartz.task.TaskRegistrationConflictPolicy;

import java.util.Map;

public record TaskRegistration(
  String taskId,
  Class<? extends Job> jobClass,
  String description,
  boolean durable,
  boolean requestRecovery,
  boolean enabled,
  TaskRegistrationConflictPolicy conflictPolicy,
  Map<String, Object> jobData,
  TaskSchedule schedule
) {}

