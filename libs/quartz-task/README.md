# toolkit4j-quartz

A lightweight high-level Quartz API for simple scheduling in Quarkus applications.

## Overview

`toolkit4j-quartz` is a thin convenience layer over Quartz.

It is designed for applications that want:

- standard Quartz as the scheduling engine
- a simpler and cleaner registration API
- less boilerplate around `JobDetail`, `Trigger`, and common lifecycle operations
- a Quarkus-friendly developer experience

This project is **not** intended to become another task runtime framework built on top of Quartz.

## Goals

The goals of this library are:

- keep Quartz as the real scheduling engine
- let users continue to write standard Quartz `Job`
- provide a concise high-level API for job registration and scheduling
- simplify common lifecycle operations such as pause, resume, trigger now, and unschedule
- keep the abstraction thin and explicit

## Non-Goals

This library intentionally does **not** aim to:

- replace the Quartz programming model
- force all executions through a custom bridge job
- require users to implement a custom task handler abstraction
- introduce a heavy runtime state manager for standard scheduling
- hide Quartz completely behind a new execution framework

If a user already understands Quartz, this library should feel like a helper layer, not a replacement.

## Design Philosophy

### Quartz remains the core

Quartz is the execution model.

Users should continue to write standard Quartz jobs.

Example:

    public class UserSyncJob implements Job {
    
      @Override
      public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Running user sync...");
      }
    }

This library should not require users to rewrite jobs into another abstraction such as `TaskHandler`.

### High-level API, not heavy abstraction

The library focuses on simplifying common tasks such as:

- register a job with a stable id
- attach cron or interval schedules
- attach job data
- enable or disable scheduling
- pause, resume, trigger now, or unschedule jobs
- inspect scheduled task metadata

### Prefer explicitness over magic

Quartz concepts such as `Job`, `JobExecutionContext`, `JobDataMap`, and `Trigger` still matter.

This library may wrap them with a cleaner fluent API, but it should not create a second execution model unless there is a strong reason.

## Core API

The main abstraction is a high-level `TaskScheduler`.

Example API:

    public interface TaskScheduler {
    
      void register(Class<? extends Job> jobClass, Consumer<TaskOptions> options);
    
      void pause(String taskId);
    
      void resume(String taskId);
    
      void triggerNow(String taskId);
    
      void unschedule(String taskId);
    
      boolean exists(String taskId);
    
      Optional<TaskInfo> getTask(String taskId);
    
      List<TaskInfo> listTasks();
    }

The key idea is simple:

- users provide a normal Quartz `Job`
- the library provides a simpler registration and management API

## Task Options

A possible `TaskOptions` API:

    public interface TaskOptions {
    
      TaskOptions id(String id);
    
      TaskOptions description(String description);
    
      TaskOptions cron(String expression);
    
      TaskOptions cron(String expression, ZoneId zoneId);
    
      TaskOptions startAt(Instant startAt);
    
      TaskOptions interval(Duration interval);
    
      TaskOptions interval(Duration interval, Instant startAt);
    
      TaskOptions jobData(String key, String value);
    
      TaskOptions jobData(String key, int value);
    
      TaskOptions jobData(String key, long value);
    
      TaskOptions jobData(String key, boolean value);
    
      TaskOptions jobData(Map<String, ?> values);
    
      TaskOptions durable(boolean durable);
    
      TaskOptions requestRecovery(boolean requestRecovery);
    
      TaskOptions enabled(boolean enabled);

      TaskOptions registrationConflictPolicy(TaskRegistrationConflictPolicy policy);

      TaskOptions ifExistsRecreate(boolean enabled);

      TaskOptions ifExistsIgnore(boolean enabled);
    }

This gives users a concise builder-style API without taking Quartz away from them.

By default, registering with an existing task id fails fast.  
Use `ifExistsRecreate(true)` to replace the existing job, or `ifExistsIgnore(true)` for idempotent registration that leaves an existing job unchanged.  
You can also set `registrationConflictPolicy(TaskRegistrationConflictPolicy.IGNORE_IF_EXISTS)` (or `RECREATE`) explicitly.

## Example Usage

### Register a cron job

    scheduler.register(UserSyncJob.class, options -> options
        .id("user-sync")
        .description("Sync users from remote system")
        .cron("0 */5 * * * ?")
        .enabled(true)
    );

### Register a one-time job

    scheduler.register(CleanupJob.class, options -> options
        .id("cleanup-once")
        .description("Run one-time cleanup")
        .startAt(Instant.now().plusSeconds(300))
        .enabled(true)
    );

### Register a fixed interval job

    scheduler.register(RefreshCacheJob.class, options -> options
        .id("refresh-cache")
        .description("Refresh local cache periodically")
        .interval(Duration.ofMinutes(10))
        .enabled(true)
    );

### Attach job data

    scheduler.register(UserSyncJob.class, options -> options
        .id("user-sync")
        .cron("0 */5 * * * ?")
        .jobData("tenantId", "default")
        .jobData("source", "remote-system")
        .enabled(true)
    );

Inside the job:

    public class UserSyncJob implements Job {
    
      @Override
      public void execute(JobExecutionContext context) {
        String tenantId = context.getMergedJobDataMap().getString("tenantId");
        String source = context.getMergedJobDataMap().getString("source");
    
        System.out.println("Syncing tenant: " + tenantId + ", source: " + source);
      }
    }

## Why This Library Does Not Use a Bridge Job as the Main Model

A common design is to introduce a unified bridge job such as:

- `QuartzBridgeJob`
- `TaskStateManager`
- custom `TaskHandler`

This project intentionally does **not** make that the primary model.

### Why

1. It makes Quartz feel hidden instead of simplified.
2. It introduces a second execution abstraction for users to learn.
3. It increases coupling between scheduling and runtime execution.
4. It pushes the project toward becoming a task framework rather than a Quartz helper library.

That is not the goal of this library.

For most applications, it is better to let the user write a normal Quartz `Job` and let this library focus on scheduling and lifecycle convenience.

## Scope

This project should stay focused on the following capabilities:

- simple registration of Quartz jobs
- cron scheduling
- interval scheduling
- one-time scheduling
- job data binding through Quartz `JobDataMap`
- pause, resume, trigger-now, and unschedule
- metadata lookup for scheduled jobs
- clean Quarkus integration

This project should avoid expanding too early into:

- custom task execution pipelines
- task handler hierarchies
- workflow engines
- distributed runtime orchestration
- large state management subsystems
- hiding Quartz internals behind a completely new model

## Quarkus Positioning

This library is intended to be a Quarkus-friendly high-level Quartz API.

That means:

- good developer ergonomics
- simple registration API
- minimal boilerplate
- clean integration with application code

That does **not** mean building a thick framework layer that completely replaces Quartz concepts.

## Summary

`toolkit4j-quartz` should be:

- a lightweight facade over Quartz
- job-first, not handler-first
- high-level, but not over-abstracted
- convenient, but still explicit
- simple enough for normal application scheduling

In short:

Write normal Quartz jobs.  
Use this library to schedule and manage them with less boilerplate.

## Dependencies

- `toolkit4j-quartz` declares Quartz as a compile-time dependency only.
- Applications must provide Quartz at runtime (for example, by adding Quartz in the application module or platform BOM).
- This keeps the toolkit module lightweight and avoids forcing runtime dependency decisions on consumers.

## JPMS

- Module name: `org.toolkit4j.quartz.task`
- This module exports: `org.toolkit4j.quartz.task`
- It requires Quartz via JPMS: `requires org.quartz`
- Applications using JPMS should ensure Quartz is present on the module path.

## Example Job

    package com.example.jobs;
    
    import org.quartz.Job;
    import org.quartz.JobExecutionContext;
    import org.quartz.JobExecutionException;
    
    public class UserSyncJob implements Job {
    
      @Override
      public void execute(JobExecutionContext context) throws JobExecutionException {
        String tenantId = context.getMergedJobDataMap().getString("tenantId");
        System.out.println("Executing user sync for tenant: " + tenantId);
      }
    }

## Example Registration

    scheduler.register(UserSyncJob.class, options -> options
        .id("user-sync")
        .description("Sync users from remote system")
        .cron("0 */5 * * * ?")
        .jobData("tenantId", "default")
        .requestRecovery(true)
        .enabled(true)
    );