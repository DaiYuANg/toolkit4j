/* (C)2023*/
package org.toolkit.spring.boot.cache.api;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Cache<K, V> extends Iterable<Cache<K, V>> {

	void put(@NotNull @NonNull K k, V v);

	void put(@NotNull @NonNull K k, V v, Duration duration);

	void put(@NotNull @NonNull K k, V v, long delay, TimeUnit timeUnit);

	void putAll(@NotNull @NonNull Map<? extends K, ? extends V> data);

	int size();

	boolean isEmpty();

	boolean containsKey(@NotNull @NonNull K k);

	boolean putIfAbsent(@NotNull @NonNull K k, V v);

	void remove(@NotNull @NonNull K k);

	void removeAll(Collection<K> keys);

	void clear();

	@NotNull @NonNull Collection<K> keys();

	@NotNull @NonNull Collection<V> values();

	@Nullable V compute(
			@NotNull @NonNull K key, @NotNull @NonNull BiFunction<? super K, ? super V, ? extends V> remappingFunction);

	default @NotNull @NonNull Optional<V> computeOptional(
			@NotNull @NonNull K key,
			@NotNull @NonNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return Optional.ofNullable(compute(key, remappingFunction));
	}

	@Nullable V computeIfAbsent(@NotNull @NonNull K key, @NotNull @NonNull Function<? super K, ? extends V> mappingFunction);

	default @NotNull @NonNull Optional<V> computeIfAbsentOptional(
			@NotNull @NonNull K key, @NotNull @NonNull Function<? super K, ? extends V> mappingFunction) {
		return Optional.ofNullable(computeIfAbsent(key, mappingFunction));
	}

	@Nullable V computeIfPresent(@NotNull @NonNull K key, Function<? super K, ? extends V> mappingFunction);

	default @NonNull Optional<V> computeIfPresentOptional(
			@NotNull @NonNull K key, @NotNull @NonNull Function<? super K, ? extends V> mappingFunction) {
		return Optional.ofNullable(computeIfPresent(key, mappingFunction));
	}

	boolean replace(@NotNull @NonNull K k, V v);

	@Nullable V get(@NotNull @NonNull K k);

	@NonNull @NotNull Map<? extends K, ? extends V> getAll();

	default Optional<V> getOptional(@NotNull @NonNull K k) {
		return Optional.ofNullable(get(k));
	}

	@Nullable V getAndRemove(@NotNull @NonNull K k);

	default Optional<V> getAndRemoveOptional(@NotNull @NonNull K k) {
		return Optional.ofNullable(getAndRemove(k));
	}

	default @NotNull @NonNull V getOrDefault(@NotNull @NonNull K k, V dv) {
		return Optional.ofNullable(get(k)).orElse(dv);
	}

	@NonNull @NotNull String getCacheName();
}
