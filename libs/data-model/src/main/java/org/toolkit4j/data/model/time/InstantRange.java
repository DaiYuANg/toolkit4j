package org.toolkit4j.data.model.time;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.toolkit4j.data.model.range.Range;

import java.time.Instant;
import java.util.Objects;

public record InstantRange(
  Range<Instant> value
) {
  public InstantRange {
    value = Objects.requireNonNull(value, "value");
  }

  public static @NotNull InstantRange unbounded() {
    return new InstantRange(Range.unbounded());
  }

  public static @NotNull InstantRange closed(@NotNull Instant lower, @NotNull Instant upper) {
    return new InstantRange(Range.closed(lower, upper));
  }

  public static @NotNull InstantRange open(@NotNull Instant lower, @NotNull Instant upper) {
    return new InstantRange(Range.open(lower, upper));
  }

  public static @NotNull InstantRange closedOpen(@NotNull Instant lower, @NotNull Instant upper) {
    return new InstantRange(Range.closedOpen(lower, upper));
  }

  public static @NotNull InstantRange openClosed(@NotNull Instant lower, @NotNull Instant upper) {
    return new InstantRange(Range.openClosed(lower, upper));
  }

  public static @NotNull InstantRange atLeast(@NotNull Instant lower) {
    return new InstantRange(Range.atLeast(lower));
  }

  public static @NotNull InstantRange greaterThan(@NotNull Instant lower) {
    return new InstantRange(Range.greaterThan(lower));
  }

  public static @NotNull InstantRange atMost(@NotNull Instant upper) {
    return new InstantRange(Range.atMost(upper));
  }

  public static @NotNull InstantRange lessThan(@NotNull Instant upper) {
    return new InstantRange(Range.lessThan(upper));
  }

  public boolean hasLowerBound() {
    return value.hasLowerBound();
  }

  public boolean hasUpperBound() {
    return value.hasUpperBound();
  }

  public boolean isEmpty() {
    return value.isEmpty();
  }

  public boolean contains(@Nullable Instant instant) {
    return value.contains(instant);
  }
}
