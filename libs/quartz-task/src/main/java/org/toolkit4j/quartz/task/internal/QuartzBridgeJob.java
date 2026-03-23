package org.toolkit4j.quartz.task.internal;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.toolkit4j.quartz.task.TriggerSource;

/**
 * Unified Quartz Job bridge: all task executions go through TaskStateManager.
 */
public class QuartzBridgeJob implements Job {

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    try {
      Object obj = context.getScheduler().getContext().get(TaskStateManager.QUARTZ_CONTEXT_KEY);
      if (!(obj instanceof TaskStateManager manager)) {
        throw new IllegalStateException("TaskStateManager not found in Quartz Scheduler context.");
      }

      String taskId = context.getMergedJobDataMap().getString(TaskStateManager.JOB_DATA_TASK_ID);
      if (taskId == null || taskId.isBlank()) {
        throw new IllegalStateException("Missing job data: " + TaskStateManager.JOB_DATA_TASK_ID);
      }

      TriggerSource source = TriggerSource.SCHEDULER;
      String sourceStr = context.getMergedJobDataMap().getString(TaskStateManager.JOB_DATA_TRIGGER_SOURCE);
      if (sourceStr != null) {
        source = TriggerSource.valueOf(sourceStr);
      }

      if (context.isRecovering()) {
        source = TriggerSource.RECOVERY;
      }

      manager.execute(taskId, source);
    } catch (Exception e) {
      throw new JobExecutionException(e);
    }
  }
}

