package org.toolkit4j.quartz.task;

import java.time.Instant;

/**
 * Latest runtime state of a task.
 */
public record TaskStatus(
    String taskId,
    String taskType,
    boolean enabled,
    boolean running,
    Instant lastStartedAt,
    Instant lastFinishedAt,
    Instant lastSuccessAt,
    Instant lastFailureAt,
    Instant nextFireAt,
    ExecutionStatus lastStatus,
    String lastErrorMessage,
    String lastErrorStackTrace,
    Long lastDurationMs,
    Integer consecutiveFailureCount
) {}
