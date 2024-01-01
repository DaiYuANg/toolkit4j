package org.toolkit4j.cache

import org.toolkit4j.cache.api.Cache
import org.toolkit4j.cache.api.CacheType
import java.util.function.Consumer
import java.util.function.Function

enum class CacheType {
    LOCAL,

    REMOTE
}

interface CacheConfig<K, V> {
    fun keyConvertor(): Function<K, Any?> {
        return Function<K, Any?> { k: K? -> k }
    }

    fun isStatisticsEnabled(): Boolean {
        return false
    }

    fun maxSize(): Int

    fun cacheType(): CacheType

    fun enableNullValue(): Boolean {
        return false
    }

    fun preloader(): Consumer<Cache<K?, V?>> {
        return Consumer<Cache<K?, V?>> { }
    }
}