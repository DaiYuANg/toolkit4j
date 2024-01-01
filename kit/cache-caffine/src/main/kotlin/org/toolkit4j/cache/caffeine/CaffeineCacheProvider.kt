package org.toolkit4j.cache.caffeine

import com.github.benmanes.caffeine.cache.Caffeine
import org.toolkit4j.cache.CacheProvider
import org.toolkit4j.cache.api.Cache
import org.toolkit4j.cache.api.CacheConfig
import java.util.concurrent.TimeUnit

class CaffeineCacheProvider : CacheProvider {
    //    override fun create(config: CacheConfig<Any, Any?>): Cache<Any, Any> {
//        if (config is CaffeineCacheConfig<*, *>) {
//            val caffineCache = Caffeine.newBuilder()
//                .expireAfterWrite(10, TimeUnit.MINUTES)
//                .maximumSize(10000)
//                .build<Any, Any>()
//            val a = CaffeineCacheKt<Any, Any>(caffineCache)
//            return a
//        }
//        throw IllegalArgumentException("Invalid configuration for Caffine cache.")
//    }
//    override fun create(config: CacheConfig<out Any, out Any?>): Cache<out Any, out Any?> {
//        if (config is CaffeineCacheConfig<*, *>) {
//            val caffineCache = Caffeine.newBuilder()
//                .expireAfterWrite(10, TimeUnit.MINUTES)
//                .maximumSize(10000)
//                .build<Any, Any>()
//            val a = CaffeineCacheKt(caffineCache)
//            return a
//        }
//        throw IllegalArgumentException("Invalid configuration for Caffine cache.")
//    }
//    override fun create(config: CacheConfig<Any, Any>): Cache<Any, Any?> {
//        if (config is CaffeineCacheConfig<*, *>) {
//            val caffineCache = Caffeine.newBuilder()
//                .expireAfterWrite(10, TimeUnit.MINUTES)
//                .maximumSize(10000)
//                .build<Any, Any?>()
//            val a = CaffeineCacheKt<Any, Any?>(caffineCache)
//            return a
//        }
//    }
}