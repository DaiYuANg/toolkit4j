package org.toolkit4j.quartz.task;

import java.time.Instant;

/**
 * One execution attempt of a task.
 */
public record TaskExecutionRecord(
    String executionId,
    String taskId,
    String taskType,
    Instant startedAt,
    Instant finishedAt,
    long durationMs,
    ExecutionStatus status,
    String errorMessage,
    String errorStackTrace,
    TriggerSource triggerSource
) {}
