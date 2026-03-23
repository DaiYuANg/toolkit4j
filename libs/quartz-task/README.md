# quartz-task Design Document

## 1. Overview

`quartz-task` is a lightweight high-level API for Quartz, focused on simple task scheduling and basic execution observability.

It is designed for common JVM application scenarios where Quartz is already the scheduling engine, but its low-level API is too verbose or fragmented for everyday task registration and management.

The library is intentionally small in scope. It is not a platform, not a workflow engine, not a control plane, and not a framework integration module.

---

## 2. Goals

### 2.1 Primary Goals

- Provide a simpler and cleaner API on top of Quartz
- Expose task-oriented concepts instead of Quartz low-level primitives
- Make common scheduling scenarios easy to use
- Support code-based task registration
- Support common schedule types:
  - cron
  - one-time execution
  - fixed interval
- Provide basic runtime observability
- Keep the API small and easy to understand
- Avoid heavy abstraction layers

### 2.2 Non-Goals

- Spring Boot integration
- Quarkus integration
- HTTP API
- Web UI
- JDBC backend
- Workflow orchestration
- DAG support
- Dynamic task type creation at runtime
- Heavy plugin or extension mechanisms
- Replacing Quartz itself

---

## 3. Design Principles

### 3.1 Quartz Is the Foundation

Quartz is the actual scheduling foundation of this library.

The purpose of `quartz-task` is not to replace Quartz or to hide every Quartz concept behind deep abstraction. The goal is to provide a thinner and cleaner high-level API for common usage.

### 3.2 Simple Usage First

The library should optimize for the most common usage style:

- define a handler
- register it
- configure it with a small builder
- schedule it
- inspect status and recent execution records

### 3.3 Task-Oriented API

The public API should revolve around tasks and handlers, not around `JobDetail`, `Trigger`, `JobKey`, or `JobDataMap`.

### 3.4 Thin Wrapper, Not Heavy Framework

The library should stay as a thin high-level wrapper over Quartz. It should not introduce platform-style architecture, over-designed layering, or unnecessary indirection.

### 3.5 Built-In Observability

Basic execution status and recent execution history are part of the core value of the library, not optional add-ons.

---

## 4. Scope

### 4.1 In Scope

- Task handler abstraction
- Simple registration API with builder-style options
- Code-based task registration only
- Scheduling with:
  - cron
  - once
  - fixed interval
- Pause task
- Resume task
- Trigger task immediately
- Unschedule task
- Query task status
- Query recent execution records

### 4.2 Out of Scope

- Framework-specific integration modules
- Dynamic registration of new task types from external sources
- Persistent backends
- HTTP endpoints
- Web UI
- Platform-level management
- Workflow or DAG orchestration
- Cross-application coordination features

---

## 5. Core Concepts

### 5.1 TaskHandler

`TaskHandler` is the main extension point for business code.

    public interface TaskHandler<T> {
    
        String type();
    
        Class<T> payloadType();
    
        void execute(TaskContext<T> context) throws Exception;
    }

#### Responsibilities

- define the task type
- declare the payload type
- execute task logic

#### Constraints

- `type()` must be unique
- implementations should be stateless or thread-safe
- handlers should not directly depend on Quartz APIs

---

### 5.2 TaskContext

`TaskContext` provides the minimal execution context for a task.

    public interface TaskContext<T> {
    
        String taskId();
    
        String taskType();
    
        T payload();
    }

This interface is intentionally minimal for the first version.

---

### 5.3 TaskBuilder

`TaskBuilder` is the main user-facing configuration API when registering a task.

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

This builder should be the primary way users configure tasks.

Example:

    scheduler.register(new UserSyncTaskHandler(), options -> options
        .id("user-sync")
        .description("Sync users from remote system")
        .cron("0 */5 * * * ?")
        .enabled(true)
    );

The builder may normalize its values into internal models, but those models do not need to be the main public API.

---

### 5.4 TaskStatus

`TaskStatus` represents the latest runtime state of a task.

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

This is the main read model for task runtime state.

---

### 5.5 TaskExecutionRecord

`TaskExecutionRecord` represents one execution attempt.

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

The first version only needs bounded in-memory recent history, not persistent audit storage.

---

### 5.6 ExecutionStatus

    public enum ExecutionStatus {
        RUNNING,
        SUCCESS,
        FAILED,
        SKIPPED
    }

`SKIPPED` is optional in the first implementation, but reserving it is acceptable.

---

### 5.7 TriggerSource

    public enum TriggerSource {
        SCHEDULER,
        MANUAL,
        RECOVERY
    }

This allows recent execution records to distinguish scheduled execution from manual triggering.

---

## 6. Public API

The public API should remain small. A single main scheduler interface is preferred.

### 6.1 TaskScheduler

    public interface TaskScheduler {
    
        void register(TaskHandler<?> handler, Consumer<TaskBuilder> options);
    
        void pause(String taskId);
    
        void resume(String taskId);
    
        void triggerNow(String taskId);
    
        void unschedule(String taskId);
    
        Optional<TaskStatus> getStatus(String taskId);
    
        List<TaskStatus> listStatuses();
    
        List<TaskExecutionRecord> getRecentExecutions(String taskId, int limit);
    }

This interface should be the main entry point for consumers.

### 6.2 API Notes

- `register(...)` registers a handler together with one task configuration
- `pause(...)` pauses a task
- `resume(...)` resumes a task
- `triggerNow(...)` runs a task immediately through the same runtime path
- `unschedule(...)` removes the task schedule
- `getStatus(...)` returns the current task status
- `listStatuses()` returns all known task statuses
- `getRecentExecutions(...)` returns recent execution records for diagnostics

No Quartz types should appear in this public API.

---

## 7. Scheduling Model

The external API should remain builder-first and simple.

Internally, the builder may still normalize user input into internal scheduling models, but these models are internal concepts rather than the main user-facing API.

The first version only needs three schedule forms:

- cron
- once
- fixed interval

The following user-facing methods are sufficient:

- `cron(String expression)`
- `cron(String expression, ZoneId zoneId)`
- `once(Instant fireAt)`
- `fixedInterval(Duration interval)`
- `fixedInterval(Duration interval, Instant startAt)`

This is enough for common usage without forcing users to understand internal scheduling types.

---

## 8. Execution Model

When Quartz fires a trigger, the library should execute a task through a unified internal flow:

1. resolve the registered task by task id
2. resolve the handler by task type
3. prepare the `TaskContext`
4. mark the task as running
5. execute the handler
6. capture success or failure
7. update task status
8. append an execution record
9. mark the task as no longer running

This unified flow is one of the main reasons the library exists. It ensures task execution behavior is consistent and observable.

---

## 9. Observability

Observability is mandatory even in a thin wrapper.

### 9.1 Required Status Fields

Each task must expose:

- whether it is currently running
- last started time
- last finished time
- last success time
- last failure time
- next fire time
- last execution result
- last execution duration
- last error message
- last error stack trace
- consecutive failure count

### 9.2 Required Execution Record Fields

Each execution record must contain:

- execution id
- task id
- task type
- start time
- finish time
- duration
- result
- trigger source
- error summary if failed
- error stack trace if failed

### 9.3 Retention Strategy

The first version should keep only recent execution records in memory.

A bounded in-memory history is sufficient. Long-term persistence is out of scope.

---

## 10. Internal Implementation Notes

### 10.1 Quartz Usage

Quartz is the actual scheduling implementation. Internally, the library will still map task registrations to Quartz jobs and triggers.

These details remain internal and should not shape the public API.

### 10.2 Unified Bridge Execution

Internally, using a single bridge job class is recommended so that all scheduled executions pass through the same runtime logic.

This keeps handler execution, status updates, and execution recording consistent.

### 10.3 In-Memory Runtime State

Runtime task status and recent execution history should be stored in memory.

This is sufficient for the intended scope and avoids unnecessary persistence abstractions.

### 10.4 Internal Scheduling Models

The implementation may use internal configuration or normalized scheduling models, but they should remain internal concepts.

The main public API should still be builder-based.

---

## 11. Error Handling

### 11.1 Registration Errors

Registration-related errors should fail fast. Examples include:

- duplicate task id
- duplicate handler type where not allowed
- invalid cron expression
- missing schedule configuration
- incompatible payload type

These should surface as clear exceptions.

### 11.2 Execution Errors

Any exception thrown by a task handler must be captured and reflected in:

- `TaskStatus`
- `TaskExecutionRecord`

A failed execution must not disappear into logs only.

### 11.3 Suggested Exceptions

Suggested internal runtime exceptions:

- `TaskRegistrationException`
- `TaskSchedulingException`
- `TaskExecutionException`
- `TaskNotFoundException`

A simple runtime-exception-based design is sufficient.

---

## 12. Minimal Consumer Experience

The intended usage style should be simple.

Example handler:

    public final class UserSyncTaskHandler implements TaskHandler<UserSyncPayload> {
    
        @Override
        public String type() {
            return "user-sync";
        }
    
        @Override
        public Class<UserSyncPayload> payloadType() {
            return UserSyncPayload.class;
        }
    
        @Override
        public void execute(TaskContext<UserSyncPayload> context) {
            // business logic
        }
    }

Example registration:

    scheduler.register(new UserSyncTaskHandler(), options -> options
        .id("user-sync")
        .description("Sync users from remote system")
        .cron("0 */5 * * * ?")
        .enabled(true)
    );

Example registration with payload:

    scheduler.register(new UserSyncTaskHandler(), options -> options
        .id("user-sync-main")
        .description("Sync users from remote system")
        .cron("0 */5 * * * ?")
        .payload(new UserSyncPayload(...))
        .enabled(true)
    );

Example manual trigger:

    scheduler.triggerNow("user-sync-main");

Example status query:

    Optional<TaskStatus> status = scheduler.getStatus("user-sync-main");

This is the intended direction: simple, task-oriented, and cleaner than direct Quartz usage.

---

## 13. Recommended Package Structure

A simple single-module package layout is enough:

    io.github.daiyuang.toolkit.task
    ├── TaskScheduler.java
    ├── TaskHandler.java
    ├── TaskContext.java
    ├── TaskBuilder.java
    ├── TaskStatus.java
    ├── TaskExecutionRecord.java
    ├── ExecutionStatus.java
    ├── TriggerSource.java
    ├── DefaultTaskScheduler.java
    ├── DefaultTaskBuilder.java
    ├── DefaultTaskContext.java
    ├── QuartzBridgeJob.java
    ├── TaskStateManager.java
    └── internal/...

The exact package split does not need to be over-designed. The project should stay small and readable.

---

## 14. MVP Scope

### 14.1 Must Have

- `TaskHandler`
- `TaskContext`
- `TaskBuilder`
- `TaskStatus`
- `TaskExecutionRecord`
- `TaskScheduler`
- cron scheduling
- once scheduling
- fixed interval scheduling
- pause
- resume
- trigger now
- unschedule
- in-memory task status
- in-memory recent execution history
- unified execution flow
- basic failure recording

### 14.2 Explicitly Out of Scope

- Spring integration
- Quarkus integration
- HTTP endpoints
- Web UI
- JDBC storage
- metrics integration
- telemetry integration
- workflow features
- dynamic task creation from configuration
- heavy SPI or plugin model

---

## 15. Summary

`quartz-task` should remain a small and direct high-level API for Quartz.

Its purpose is not to replace Quartz, not to build a platform, and not to introduce complex extension mechanisms. Its purpose is simply to make common Quartz-based task scheduling cleaner, easier to use, and easier to observe.

The project should stay intentionally small, task-oriented, builder-first, and focused on simple usage.