package org.toolkit4j.quartz.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TaskInfoBuilderTest {

  @Test
  void taskInfoBuilderBuildsMetadataRecord() {
    Instant startAt = Instant.parse("2026-03-27T00:00:00Z");
    Instant nextFireAt = startAt.plusSeconds(60);

    TaskInfo info =
        TaskInfoBuilder.builder()
            .taskId("demo")
            .jobClass(DefaultTaskSchedulerTest.ManualTriggerJob.class)
            .description("demo task")
            .durable(true)
            .requestRecovery(true)
            .scheduleType(TaskScheduleKind.INTERVAL)
            .cronZoneId(ZoneId.of("UTC"))
            .interval(Duration.ofMinutes(5))
            .startAt(startAt)
            .jobData(Map.of("tenantId", "t-1"))
            .nextFireAt(nextFireAt)
            .paused(false)
            .build();

    assertEquals("demo", info.taskId());
    assertEquals(TaskScheduleKind.INTERVAL, info.scheduleType());
    assertEquals(Duration.ofMinutes(5), info.interval());
    assertEquals("t-1", info.jobData().get("tenantId"));
    assertEquals(nextFireAt, info.nextFireAt());
    assertTrue(info.requestRecovery());
  }

  @Test
  void scheduleMetadataBuilderBuildsPackagePrivateRecord() {
    ScheduleMetadata metadata =
        ScheduleMetadataBuilder.builder()
            .kind(TaskScheduleKind.CRON)
            .cronExpression("0/15 * * * * ?")
            .cronZoneId(ZoneId.of("UTC"))
            .startAt(Instant.parse("2026-03-27T00:00:00Z"))
            .build();

    assertEquals(TaskScheduleKind.CRON, metadata.kind());
    assertEquals("0/15 * * * * ?", metadata.cronExpression());
  }
}
