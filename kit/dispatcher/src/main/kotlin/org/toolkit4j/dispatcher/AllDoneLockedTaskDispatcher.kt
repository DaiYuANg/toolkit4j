package org.toolkit4j.dispatcher

import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger


private data class TaskBatchStore(
    val batchId: Int,
    val queue: BlockingQueue<Task>
)

enum class TaskPollVelocity(val velocity: Long) {
    FAST(10),

    MEDIA(100),

    SLOW(1000);
}

data class AllDoneLockedTaskDispatcher @JvmOverloads constructor(
    private var scheduledThreadPoolExecutor: ScheduledThreadPoolExecutor? = null,
) {
    private val autoincrementIdForBatchStore = AtomicInteger(0)

    private val taskStoreConcurrentMap: ConcurrentMap<Int, TaskBatchStore> = ConcurrentHashMap()

    private val runningDispatcher: ConcurrentMap<Int, ScheduledFuture<*>> = ConcurrentHashMap()

    private val doneIds: CopyOnWriteArraySet<Int> = CopyOnWriteArraySet<Int>()

    init {
        scheduledThreadPoolExecutor = scheduledThreadPoolExecutor ?: ScheduledThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(), Thread.ofVirtual()
                .name(this.javaClass.getSimpleName(), 0)
                .factory()
        )
    }

    fun batchTask(tasks: Collection<Task>): Int {
        val storeId: Int = autoincrementIdForBatchStore.getAndIncrement()
        val queue: LinkedBlockingQueue<Task> = LinkedBlockingQueue<Task>(tasks.size)
            .also { it.addAll(tasks) }
        val store = TaskBatchStore(
            batchId = storeId,
            queue = queue
        )
        taskStoreConcurrentMap[storeId] = store
        return storeId;
    }

    fun startDispatcher(id: Int, velocity: TaskPollVelocity, whenAllDown: Runnable = Runnable { }) {
        val store = Optional.ofNullable<TaskBatchStore>(taskStoreConcurrentMap[id]).orElseThrow {
            RuntimeException(
                "Task id is not exists"
            )
        }
        val scheduledFuture = scheduledThreadPoolExecutor?.schedule({
            val task = store.queue.poll()
            if (task == null) {
                runningDispatcher[store.batchId]?.cancel(false)
                doneIds.add(store.batchId)
                whenAllDown.run()
                return@schedule
            }
            val locker = Objects.requireNonNull(task).tryLock
            if (locker.get()) {
                task.action.run()
            } else {
                if (!store.queue.offer(task)) {
                    throw RuntimeException("Re enter queue fail")
                }
            }
        }, velocity.velocity, TimeUnit.MILLISECONDS)
        runningDispatcher[store.batchId] = scheduledFuture
    }

    fun startDispatcher(velocity: TaskPollVelocity = TaskPollVelocity.MEDIA) {
        taskStoreConcurrentMap.forEach { (t, _) ->
            startDispatcher(
                t, velocity
            )
        }
    }

    fun isDone(id: Int) {

    }
}