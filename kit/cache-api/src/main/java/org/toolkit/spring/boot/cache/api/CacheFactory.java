/* (C)2023*/
package org.toolkit.spring.boot.cache.api;

public interface CacheFactory<K, V> {

	Cache<K, V> create(CacheConfig<K, V> config);
}
