package org.toolkit4j.quartz.task;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

/**
 * Builder for task registration configuration.
 */
public interface TaskBuilder {

  TaskBuilder id(String id);

  TaskBuilder description(String description);

  TaskBuilder cron(String expression);

  TaskBuilder cron(String expression, ZoneId zoneId);

  TaskBuilder once(Instant fireAt);

  TaskBuilder fixedInterval(Duration interval);

  TaskBuilder fixedInterval(Duration interval, Instant startAt);

  TaskBuilder payload(Object payload);

  TaskBuilder enabled(boolean enabled);
}
