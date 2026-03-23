package org.toolkit4j.quartz.task.internal;

import org.toolkit4j.quartz.task.TaskContext;

public record DefaultTaskContext<T>(String taskId, String taskType, T payload) implements TaskContext<T> {}

