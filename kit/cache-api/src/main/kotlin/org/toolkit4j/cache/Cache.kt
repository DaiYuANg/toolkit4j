package org.toolkit4j.cache

import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.BiFunction
import java.util.function.Function
import java.util.stream.Stream

interface Cache<K, V : Any?> : MutableIterable<Map.Entry<K, V>> {
    fun put(k: K, v: V?)

    fun put(k: K, v: V?, duration: Duration)

    fun put(k: K, v: V?, delay: Long, timeUnit: TimeUnit)

    fun putAll(data: Map<out K, V?>)

    fun putAll(data: Map<out K, V?>, duration: Duration)

    fun putAll(data: Map<out K, V?>, delay: Long, timeUnit: TimeUnit)

    fun size(): Int

    fun isEmpty(): Boolean

    fun containsKey(k: K): Boolean

    fun putIfAbsent(k: K, v: V?): Boolean

    fun remove(k: K)

    fun removeAll(keys: Collection<K>)

    fun clear()

    fun keys(): Collection<K>

    fun values(): Collection<V>

    fun compute(
        key: K, remappingFunction: BiFunction<in K, in V, out V>
    ): V?

    fun computeOptional(
        key: K,
        remappingFunction: BiFunction<in K, in V, out V>
    ): Optional<V & Any> {
        return Optional.ofNullable(compute(key, remappingFunction))
    }

    fun computeIfAbsent(key: K, mappingFunction: Function<in K, out V>): V?

    fun computeIfAbsentOptional(
        key: K, mappingFunction: Function<in K, out V>
    ): Optional<V & Any> {
        return Optional.ofNullable(computeIfAbsent(key, mappingFunction))
    }

    fun computeIfPresent(key: K, mappingFunction: Function<in K, out V>?): V?

    fun computeIfPresentOptional(
        key: K, mappingFunction: Function<in K, out V>
    ): Optional<V & Any> {
        return Optional.ofNullable(computeIfPresent(key, mappingFunction))
    }

    fun replace(k: K, v: V): Boolean

    fun get(k: K): V?

    fun getAll(): Map<out K, V>

    fun getOptional(k: K): Optional<V & Any> {
        return Optional.ofNullable(get(k))
    }

    fun getAndRemove(k: K): V?

    fun getAndRemoveOptional(k: K): Optional<V & Any> {
        return Optional.ofNullable(getAndRemove(k))
    }

    fun getOrDefault(k: K, v: V): V {
        return Optional.ofNullable(get(k)).orElse(v)
    }

    fun getCacheName(): String

    fun stream(): Stream<Map.Entry<K, V>>

    fun registerListener(listener: CacheListener<K, V>)

    fun unregisterListener(listener: CacheListener<K, V>)
}