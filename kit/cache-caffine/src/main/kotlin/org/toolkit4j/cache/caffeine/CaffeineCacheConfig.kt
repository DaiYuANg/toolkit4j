package org.toolkit4j.cache.caffeine

import org.toolkit4j.cache.CacheConfig
import org.toolkit4j.cache.api.CacheType

interface CaffeineCacheConfig<K,V>:CacheConfig<K,V> {
    override fun cacheType(): CacheType {
        return CacheType.LOCAL
    }
}