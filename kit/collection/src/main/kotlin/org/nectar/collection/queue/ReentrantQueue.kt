package org.nectar.collection.queue

import java.util.Queue

class ReentrantQueue<T, Q : Queue<T>>(private val queue: Q) : Queue<T> by queue {
    fun reentrant() {
        val value = queue.poll()
        queue.offer(value)
    }
}
