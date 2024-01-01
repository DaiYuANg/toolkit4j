/* (C)2023*/
package org.toolkit4j.cache.api;

import java.util.function.Consumer;
import java.util.function.Function;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Accessors(chain = true)
public interface CacheConfig<K, V> {

    default Function<K, Object> keyConvertor() {
        return (k) -> k;
    }

    default boolean isStatisticsEnabled() {
        return false;
    }

    int maxSize();

    CacheType cacheType();

    default boolean enableNullValue() {
        return false;
    }

    default Consumer<Cache<K, V>> preloader() {
        return (cache) -> {

        };
    }
}
