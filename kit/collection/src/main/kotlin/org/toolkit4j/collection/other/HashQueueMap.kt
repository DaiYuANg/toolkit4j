package org.toolkit4j.collection.other

import java.util.Queue
import java.util.concurrent.LinkedBlockingQueue
import kotlin.collections.HashMap

data class HashQueueMap<K, T> @JvmOverloads constructor(
    private val internal: HashMap<K, Queue<T>> = HashMap(),
) : MutableMap<K, Queue<T>> by internal {
    fun element(key: K?, value: T): T? {
        return internal[key]?.element()
    }

    fun poll(key: K?): T? {
        return internal[key]?.poll()
    }

    fun offer(key: K?, value: T) {
        key?.let { internal.computeIfAbsent(it) { LinkedBlockingQueue() }.offer(value) }
    }

    fun add(key: K?, value: T) {
        key?.let { internal.computeIfAbsent(it) { LinkedBlockingQueue() }.add(value) }
    }

    fun peek(key: K?) {
        internal[key]?.peek()
    }
}