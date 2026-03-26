package org.toolkit4j.data.model.range;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record Bound<T extends Comparable<? super T>>(
  T value,
  BoundType type
) {
  public Bound {
    value = Objects.requireNonNull(value, "value");
    type = Objects.requireNonNull(type, "type");
  }

  public static <T extends Comparable<? super T>> @NotNull Bound<T> open(@NotNull T value) {
    return new Bound<>(value, BoundType.OPEN);
  }

  public static <T extends Comparable<? super T>> @NotNull Bound<T> closed(@NotNull T value) {
    return new Bound<>(value, BoundType.CLOSED);
  }
}
