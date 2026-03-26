package org.toolkit4j.quartz.task.internal;

import org.junit.jupiter.api.Test;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.toolkit4j.quartz.task.TaskRegistrationConflictPolicy;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InternalRecordBuilderTest {

  @Test
  void taskScheduleBuilderBuildsInternalRecord() {
    TaskSchedule schedule = TaskScheduleBuilder.builder()
      .type(TaskScheduleType.CRON)
      .cronExpression("0 0 * * * ?")
      .cronZoneId(ZoneId.of("UTC"))
      .build();

    assertEquals(TaskScheduleType.CRON, schedule.type());
    assertEquals("0 0 * * * ?", schedule.cronExpression());
  }

  @Test
  void taskRegistrationBuilderBuildsInternalRecord() {
    TaskRegistration registration = TaskRegistrationBuilder.builder()
      .taskId("demo")
      .jobClass(DemoJob.class)
      .description("demo registration")
      .durable(true)
      .requestRecovery(false)
      .conflictPolicy(TaskRegistrationConflictPolicy.FAIL)
      .jobData(Map.of("tenantId", "t-1"))
      .schedule(TaskSchedule.fixedInterval(Duration.ofMinutes(1), Instant.parse("2026-03-27T00:00:00Z")))
      .build();

    assertEquals("demo", registration.taskId());
    assertEquals("demo registration", registration.description());
    assertEquals("t-1", registration.jobData().get("tenantId"));
  }

  public static class DemoJob implements Job {
    @Override
    public void execute(JobExecutionContext context) {
    }
  }
}
