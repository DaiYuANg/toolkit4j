package org.toolkit4j.cache.caffeine

import com.github.benmanes.caffeine.cache.Cache
import org.toolkit4j.cache.CacheListener
import java.time.Duration
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.TimeUnit
import java.util.function.BiFunction
import java.util.function.Function
import java.util.stream.Stream

class CaffeineCacheKt<K : Any, V : Any?>(
    private val caffeineInstance: Cache<K, V>
) : org.toolkit4j.cache.Cache<K, V>, Cache<K, V> by caffeineInstance {
    private val listeners: CopyOnWriteArraySet<CacheListener<K, V>> by lazy {
        CopyOnWriteArraySet()
    }

    override fun put(k: K, v: V?) {
        caffeineInstance.put(k, v)
    }

    override fun put(k: K, v: V?, duration: Duration) {
        caffeineInstance.policy().expireVariably().ifPresent {
            it.put(k, v, duration)
        }
    }

    override fun put(k: K, v: V?, delay: Long, timeUnit: TimeUnit) {
        TODO("Not yet implemented")
    }

    override fun putAll(data: Map<out K, V?>) {
        TODO("Not yet implemented")
    }

    override fun putAll(data: Map<out K, V?>, duration: Duration) {
        TODO("Not yet implemented")
    }

    override fun putAll(data: Map<out K, V?>, delay: Long, timeUnit: TimeUnit) {
        TODO("Not yet implemented")
    }

    override fun size(): Int {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun keys(): Collection<K> {
        TODO("Not yet implemented")
    }

    override fun values(): Collection<V> {
        TODO("Not yet implemented")
    }

    override fun getAll(): Map<out K, V> {
        TODO("Not yet implemented")
    }

    override fun getCacheName(): String {
        TODO("Not yet implemented")
    }

    override fun stream(): Stream<Map.Entry<K, V>> {
        TODO("Not yet implemented")
    }

    override fun unregisterListener(listener: CacheListener<K, V>) {
        TODO("Not yet implemented")
    }

    override fun registerListener(listener: CacheListener<K, V>) {
        TODO("Not yet implemented")
    }

    override fun iterator(): MutableIterator<Map.Entry<K, V>> {
        TODO("Not yet implemented")
    }

    override fun getAndRemove(k: K): V? {
        TODO("Not yet implemented")
    }

    override fun get(k: K): V? {
        TODO("Not yet implemented")
    }

    override fun replace(k: K, v: V): Boolean {
        TODO("Not yet implemented")
    }

    override fun computeIfPresent(key: K, mappingFunction: Function<in K, out V>?): V? {
        TODO("Not yet implemented")
    }

    override fun computeIfAbsent(key: K, mappingFunction: Function<in K, out V>): V? {
        TODO("Not yet implemented")
    }

    override fun compute(key: K, remappingFunction: BiFunction<in K, in V, out V>): V? {
        TODO("Not yet implemented")
    }

    override fun removeAll(keys: Collection<K>) {
        TODO("Not yet implemented")
    }

    override fun remove(k: K) {
        TODO("Not yet implemented")
    }

    override fun putIfAbsent(k: K, v: V?): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsKey(k: K): Boolean {
        TODO("Not yet implemented")
    }
}