package org.toolkit4j.data.model.enumeration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class EnumValues {
  private final ConcurrentMap<Class<?>, EnumLookup<?, ?>> LOOKUPS = new ConcurrentHashMap<>();

  @SuppressWarnings("unchecked")
  public <T, E extends Enum<E> & EnumValue<T>> @NotNull EnumLookup<T, E> lookup(
      @NotNull Class<E> enumClass) {
    val existing = LOOKUPS.get(enumClass);
    if (existing != null) {
      return (EnumLookup<T, E>) existing;
    }
    val created = EnumLookup.of(enumClass);
    val previous = LOOKUPS.putIfAbsent(enumClass, created);
    return (EnumLookup<T, E>) (previous != null ? previous : created);
  }
}
