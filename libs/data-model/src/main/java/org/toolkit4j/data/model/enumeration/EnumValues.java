package org.toolkit4j.data.model.enumeration;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@UtilityClass
public class EnumValues {
  private final ConcurrentMap<Class<?>, EnumLookup<?, ?>> LOOKUPS = new ConcurrentHashMap<>();

  @SuppressWarnings("unchecked")
  public <T, E extends Enum<E> & EnumValue<T>> @NotNull EnumLookup<T, E> lookup(@NotNull Class<E> enumClass) {
    var existing = LOOKUPS.get(enumClass);
    if (existing != null) {
      return (EnumLookup<T, E>) existing;
    }
    var created = EnumLookup.of(enumClass);
    var previous = LOOKUPS.putIfAbsent(enumClass, created);
    return (EnumLookup<T, E>) (previous != null ? previous : created);
  }
}
