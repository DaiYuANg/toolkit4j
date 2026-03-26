package org.toolkit4j.data.model.enumeration;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class EnumLookup<T, E extends Enum<E> & EnumValue<T>> {
  private final Class<E> enumClass;
  private final Map<T, E> values;

  private EnumLookup(Class<E> enumClass) {
    this.enumClass = Objects.requireNonNull(enumClass, "enumClass");
    this.values = buildLookup(enumClass);
  }

  public static <T, E extends Enum<E> & EnumValue<T>> @NotNull EnumLookup<T, E> of(@NotNull Class<E> enumClass) {
    return new EnumLookup<>(enumClass);
  }

  public @NotNull Class<E> enumType() {
    return enumClass;
  }

  public @NotNull E fromPrimaryValue(T value) {
    Objects.requireNonNull(value, "value");
    return findByPrimaryValue(value).orElseThrow(() ->
      new IllegalArgumentException(
        "No enum constant in " + enumClass.getSimpleName() + " for primary value: " + value
      )
    );
  }

  public @NotNull Optional<E> findByPrimaryValue(T value) {
    if (value == null) {
      return Optional.empty();
    }
    return Optional.ofNullable(values.get(value));
  }

  public boolean containsPrimaryValue(T value) {
    return findByPrimaryValue(value).isPresent();
  }

  public @NotNull List<T> primaryValues() {
    return List.copyOf(values.keySet());
  }

  public @NotNull List<E> values() {
    return List.copyOf(values.values());
  }

  public @NotNull Map<T, E> asMap() {
    return values;
  }

  private static <T, E extends Enum<E> & EnumValue<T>> Map<T, E> buildLookup(Class<E> enumClass) {
    var constants = Objects.requireNonNull(enumClass.getEnumConstants(), "enum constants");
    var lookup = new LinkedHashMap<T, E>(constants.length);
    for (var constant : constants) {
      var primaryValue = constant.getPrimaryValue();
      if (primaryValue == null) {
        throw new IllegalArgumentException("Primary value must not be null: " + constant.name());
      }
      var previous = lookup.putIfAbsent(primaryValue, constant);
      if (previous != null) {
        throw new IllegalArgumentException(
          "Duplicate primary value [%s] in %s".formatted(primaryValue, enumClass.getName())
        );
      }
    }
    return Map.copyOf(lookup);
  }
}
