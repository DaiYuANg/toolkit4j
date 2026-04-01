package org.toolkit4j.data.model.enumeration;

import static java.util.Objects.requireNonNull;

import java.util.*;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class EnumLookup<T, E extends Enum<E> & EnumValue<T>> {
  private final Class<E> enumClass;
  private final Map<T, E> values;

  private EnumLookup(Class<E> enumClass) {
    this.enumClass = requireNonNull(enumClass, "enumClass");
    this.values = buildLookup(enumClass);
  }

  public static <T, E extends Enum<E> & EnumValue<T>> @NotNull EnumLookup<T, E> of(
      @NotNull Class<E> enumClass) {
    return new EnumLookup<>(enumClass);
  }

  public @NotNull Class<E> enumType() {
    return enumClass;
  }

  public @NotNull E fromPrimaryValue(T value) {
    requireNonNull(value, "value");
    return findByPrimaryValue(value)
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "No enum constant in "
                        + enumClass.getSimpleName()
                        + " for primary value: "
                        + value));
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

  public @NotNull @Unmodifiable List<T> primaryValues() {
    return List.copyOf(values.keySet());
  }

  public @NotNull @Unmodifiable List<E> values() {
    return List.copyOf(values.values());
  }

  public @NotNull Map<T, E> asMap() {
    return values;
  }

  private static <T, E extends Enum<E> & EnumValue<T>> @Unmodifiable @NotNull Map<T, E> buildLookup(
      @NotNull Class<E> enumClass) {
    val constants = requireNonNull(enumClass.getEnumConstants(), "enum constants");
    val lookup = new LinkedHashMap<T, E>(constants.length);
    Arrays.stream(constants)
        .forEach(
            constant -> {
              val primaryValue = constant.getPrimaryValue();
              if (primaryValue == null) {
                throw new IllegalArgumentException(
                    "Primary value must not be null: " + constant.name());
              }
              val previous = lookup.putIfAbsent(primaryValue, constant);
              if (previous != null) {
                throw new IllegalArgumentException(
                    "Duplicate primary value [%s] in %s"
                        .formatted(primaryValue, enumClass.getName()));
              }
            });
    return Map.copyOf(lookup);
  }
}
