package org.toolkit4j.quartz.task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.SAME_THREAD)
class DefaultTaskSchedulerTest {

  private Scheduler scheduler;
  private DefaultTaskScheduler taskScheduler;

  private static Scheduler createTestScheduler() throws SchedulerException {
    Properties props = new Properties();
    props.setProperty("org.quartz.scheduler.instanceName", "toolkit4j-test-scheduler-" + System.nanoTime());
    props.setProperty("org.quartz.threadPool.threadCount", "2");
    props.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
    return new StdSchedulerFactory(props).getScheduler();
  }

  @AfterEach
  void tearDown() throws SchedulerException {
    if (scheduler != null) {
      scheduler.shutdown(true);
    }
  }

  @Test
  void testOnceSchedule_executesAndRecordsStatus() throws Exception {
    scheduler = createTestScheduler();
    taskScheduler = new DefaultTaskScheduler(scheduler);

    CountDownLatch latch = new CountDownLatch(1);

    TaskHandler<String> handler = new TaskHandler<>() {
      @Override
      public String type() {
        return "test-type";
      }

      @Override
      public Class<String> payloadType() {
        return String.class;
      }

      @Override
      public void execute(TaskContext<String> context) {
        assertEquals("t1", context.taskId());
        assertEquals("test-type", context.taskType());
        assertEquals("payload-1", context.payload());
        latch.countDown();
      }
    };

    taskScheduler.register(handler, options -> options
      .id("t1")
      .description("once-test")
      .once(Instant.now().plusMillis(200))
      .payload("payload-1")
      .enabled(true)
    );

    assertTrue(latch.await(5, TimeUnit.SECONDS), "task should execute");

    Optional<TaskStatus> statusOpt = taskScheduler.getStatus("t1");
    assertTrue(statusOpt.isPresent());
    TaskStatus status = statusOpt.get();
    assertEquals(ExecutionStatus.SUCCESS, status.lastStatus());
    assertNotNull(status.lastFinishedAt());

    List<TaskExecutionRecord> records = taskScheduler.getRecentExecutions("t1", 10);
    assertFalse(records.isEmpty());
    TaskExecutionRecord record = records.get(0);
    assertEquals("t1", record.taskId());
    assertEquals("test-type", record.taskType());
    assertEquals(ExecutionStatus.SUCCESS, record.status());
    assertEquals(TriggerSource.SCHEDULER, record.triggerSource());
  }

  @Test
  void testTriggerNow_executesWithManualTriggerSource() throws Exception {
    scheduler = createTestScheduler();
    taskScheduler = new DefaultTaskScheduler(scheduler);

    CountDownLatch latch = new CountDownLatch(1);

    TaskHandler<String> handler = new TaskHandler<>() {
      @Override
      public String type() {
        return "test-type-2";
      }

      @Override
      public Class<String> payloadType() {
        return String.class;
      }

      @Override
      public void execute(TaskContext<String> context) {
        latch.countDown();
      }
    };

    taskScheduler.register(handler, options -> options
      .id("t2")
      .description("manual-trigger-test")
      .once(Instant.now().plusSeconds(30)) // far in the future
      .payload("payload-2")
      .enabled(true)
    );

    taskScheduler.triggerNow("t2");
    assertTrue(latch.await(5, TimeUnit.SECONDS), "task should execute by triggerNow");

    List<TaskExecutionRecord> records = List.of();
    long deadline = System.currentTimeMillis() + 3000;
    while (System.currentTimeMillis() < deadline) {
      records = taskScheduler.getRecentExecutions("t2", 10);
      if (!records.isEmpty()) break;
      Thread.sleep(50);
    }
    assertFalse(records.isEmpty(), "recent executions should be recorded");
    TaskExecutionRecord record = records.get(0);
    assertEquals("t2", record.taskId());
    assertEquals("test-type-2", record.taskType());
    assertEquals(ExecutionStatus.SUCCESS, record.status());
    assertEquals(TriggerSource.MANUAL, record.triggerSource());
  }

  @Test
  void testUnschedule_removesTask() throws Exception {
    scheduler = createTestScheduler();
    taskScheduler = new DefaultTaskScheduler(scheduler);

    TaskHandler<String> handler = new TaskHandler<>() {
      @Override
      public String type() {
        return "test-type-3";
      }

      @Override
      public Class<String> payloadType() {
        return String.class;
      }

      @Override
      public void execute(TaskContext<String> context) {}
    };

    taskScheduler.register(handler, options -> options
      .id("t3")
      .description("unschedule-test")
      .once(Instant.now().plusSeconds(30))
      .payload("payload-3")
      .enabled(true)
    );

    assertTrue(taskScheduler.getStatus("t3").isPresent());
    taskScheduler.unschedule("t3");
    assertTrue(taskScheduler.getStatus("t3").isEmpty());
    assertTrue(taskScheduler.getRecentExecutions("t3", 10).isEmpty());
  }
}

