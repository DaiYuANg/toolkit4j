/* (C)2023*/
package org.toolkit.spring.boot.cache.api;

import java.util.concurrent.CopyOnWriteArraySet;
import lombok.ToString;

@ToString
public class MultiLevelCacheManager {

	private final CopyOnWriteArraySet<CacheManager> cacheManagers = new CopyOnWriteArraySet<>();

	public MultiLevelCacheManager() {
		//		Caching.getCachingProviders().forEach(cachingProvider ->
		// cacheManagers.add(cachingProvider.getCacheManager()));
	}

	public void createCache(String cacheName) {
		//        new MutableConfiguration<Object,Object>().set
		//		for (CacheManager cacheManager : cacheManagers) {
		//			cacheManager.createCache(cacheName, new MutableConfiguration<>());
		//		}
	}
}
