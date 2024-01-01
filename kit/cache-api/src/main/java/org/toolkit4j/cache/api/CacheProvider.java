/* (C)2023*/
package org.toolkit4j.cache.api;

public interface CacheProvider<K, V> {

    Cache<K, V> create(CacheConfig<K, V> config);
}
