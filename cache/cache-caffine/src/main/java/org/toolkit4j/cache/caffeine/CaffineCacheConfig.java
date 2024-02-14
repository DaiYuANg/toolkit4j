package org.toolkit4j.cache.caffeine;

import org.toolkit4j.cache.api.CacheConfig;
import org.toolkit4j.cache.api.CacheType;

public interface CaffineCacheConfig<K, V> extends CacheConfig<K, V> {

    @Override
    default CacheType cacheType() {
        return CacheType.LOCAL;
    }
}
