package org.toolkit4j.cache

import org.toolkit4j.cache.api.Cache
import org.toolkit4j.cache.api.CacheConfig

interface CacheProvider {
    fun create(config: CacheConfig<*, *>): Cache<*, *>
}