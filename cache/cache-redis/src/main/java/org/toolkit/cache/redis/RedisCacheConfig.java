package org.toolkit.cache.redis;

import org.toolkit4j.cache.api.CacheConfig;

public interface RedisCacheConfig<K, V> extends CacheConfig<K, V> {

    String redisHost();

    int redisPort();

    String redisPassword();

    boolean enablePool();
}
