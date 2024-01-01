/* (C)2023*/
package org.toolkit4j.cache.caffeine;

import com.github.benmanes.caffeine.cache.Policy;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.toolkit4j.cache.api.Cache;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class CaffineCache<K, V> implements Cache<K, V> {

    private final com.github.benmanes.caffeine.cache.Cache<K, V> caffeineCache;

    @Override
    public void put(@NotNull @NonNull K k, V v) {
        caffeineCache.put(k, v);
    }

    @Override
    public void put(@NotNull @NonNull K k, V v, Duration duration) {
        putExpired(a -> a.put(k, v, duration));
    }

    @Override
    public void put(@NotNull @NonNull K k, V v, long delay, TimeUnit timeUnit) {
        putExpired(a -> a.put(k, v, delay, timeUnit));
    }

    @Override
    public void putAll(@NotNull @NonNull Map<? extends K, ? extends V> data) {
        caffeineCache.putAll(data);
    }

    @Override
    public int size() {
        return caffeineCache.asMap().size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(@NotNull @NonNull K k) {
        return caffeineCache.getIfPresent(k) == null;
    }

    @Override
    public boolean putIfAbsent(@NotNull @NonNull K k, V v) {
        return caffeineCache.asMap().putIfAbsent(k, v) == null;
    }

    @Override
    public void remove(@NotNull @NonNull K k) {
        caffeineCache.invalidate(k);
    }

    @Override
    public void removeAll(Collection<K> keys) {
        caffeineCache.invalidateAll(keys);
    }

    @Override
    public void clear() {
        caffeineCache.invalidateAll();
    }

    @Override
    public @NotNull @NonNull Collection<K> keys() {
        return caffeineCache.asMap().keySet();
    }

    @Override
    public @NotNull @NonNull Collection<V> values() {
        return caffeineCache.asMap().values();
    }

    @Override
    public @Nullable V compute(@NotNull @NonNull K key, @NotNull @NonNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return caffeineCache.asMap().compute(key, remappingFunction);
    }

    @Override
    public @Nullable V computeIfAbsent(@NotNull @NonNull K key, @NotNull @NonNull Function<? super K, ? extends V> mappingFunction) {
        return caffeineCache.asMap().computeIfAbsent(key, mappingFunction);
    }

    @Override
    public @Nullable V computeIfPresent(@NotNull @NonNull K key, Function<? super K, ? extends V> mappingFunction) {
        return caffeineCache.asMap().computeIfPresent(key, (k, v) -> mappingFunction.apply(k));
    }

    @Override
    public boolean replace(@NotNull @NonNull K k, V v) {
        return caffeineCache.asMap().replace(k, v) != null;
    }

    @Override
    public @Nullable V get(@NotNull @NonNull K k) {
        return caffeineCache.getIfPresent(k);
    }

    @Override
    public @NonNull @NotNull Map<? extends K, ? extends V> getAll() {
        return new HashMap<>(caffeineCache.asMap());
    }

    @Override
    public @Nullable V getAndRemove(@NotNull @NonNull K k) {
        return null;
    }

    @Override
    public @NonNull @NotNull String getCacheName() {
        return null;
    }

    @NotNull
    @Override
    public Iterator<Cache<K, V>> iterator() {
        return null;
    }

    private void putExpired(Consumer<Policy.VarExpiration<K, V>> consumer) {
        caffeineCache.policy().expireVariably().ifPresent(consumer);
    }
}
