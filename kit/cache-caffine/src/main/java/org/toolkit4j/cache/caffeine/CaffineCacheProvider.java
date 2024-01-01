package org.toolkit4j.cache.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.auto.service.AutoService;
import lombok.val;
import org.toolkit4j.cache.api.Cache;
import org.toolkit4j.cache.api.CacheConfig;
import org.toolkit4j.cache.api.CacheProvider;

import java.util.concurrent.TimeUnit;

@AutoService(CacheProvider.class)
public class CaffineCacheProvider<K, V> implements CacheProvider<K, V> {
    @Override
    public Cache<K, V> create(CacheConfig<K, V> config) {
        if (config instanceof CaffineCacheConfig<K, V>) {
            val caffineCache = Caffeine.newBuilder()
                    .expireAfterWrite(10, TimeUnit.MINUTES)
                    .maximumSize(10_000)
                    .<K, V>build();

            return new CaffineCache<>(caffineCache);
        }
        throw new IllegalArgumentException("Invalid configuration for Caffine cache.");
    }
}
