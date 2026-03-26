package org.toolkit4j.data.model.time;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.toolkit4j.data.model.range.Range;

import java.time.LocalDate;
import java.util.Objects;

public record LocalDateRange(
  Range<LocalDate> value
) {
  public LocalDateRange {
    value = Objects.requireNonNull(value, "value");
  }

  public static @NotNull LocalDateRange unbounded() {
    return new LocalDateRange(Range.unbounded());
  }

  public static @NotNull LocalDateRange closed(@NotNull LocalDate lower, @NotNull LocalDate upper) {
    return new LocalDateRange(Range.closed(lower, upper));
  }

  public static @NotNull LocalDateRange open(@NotNull LocalDate lower, @NotNull LocalDate upper) {
    return new LocalDateRange(Range.open(lower, upper));
  }

  public static @NotNull LocalDateRange closedOpen(@NotNull LocalDate lower, @NotNull LocalDate upper) {
    return new LocalDateRange(Range.closedOpen(lower, upper));
  }

  public static @NotNull LocalDateRange openClosed(@NotNull LocalDate lower, @NotNull LocalDate upper) {
    return new LocalDateRange(Range.openClosed(lower, upper));
  }

  public static @NotNull LocalDateRange atLeast(@NotNull LocalDate lower) {
    return new LocalDateRange(Range.atLeast(lower));
  }

  public static @NotNull LocalDateRange greaterThan(@NotNull LocalDate lower) {
    return new LocalDateRange(Range.greaterThan(lower));
  }

  public static @NotNull LocalDateRange atMost(@NotNull LocalDate upper) {
    return new LocalDateRange(Range.atMost(upper));
  }

  public static @NotNull LocalDateRange lessThan(@NotNull LocalDate upper) {
    return new LocalDateRange(Range.lessThan(upper));
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

  public boolean contains(@Nullable LocalDate date) {
    return value.contains(date);
  }
}
