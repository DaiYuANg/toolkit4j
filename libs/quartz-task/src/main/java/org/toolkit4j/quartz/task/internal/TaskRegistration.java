package org.toolkit4j.quartz.task.internal;

import io.soabase.recordbuilder.core.RecordBuilder;
import java.util.Map;
import org.quartz.Job;
import org.toolkit4j.quartz.task.TaskRegistrationConflictPolicy;

@RecordBuilder
public record TaskRegistration(
    String taskId,
    Class<? extends Job> jobClass,
    String description,
    boolean durable,
    boolean requestRecovery,
    TaskRegistrationConflictPolicy conflictPolicy,
    Map<String, Object> jobData,
    TaskSchedule schedule) {}
