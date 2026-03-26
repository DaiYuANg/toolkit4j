package org.toolkit4j.data.model.time;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.toolkit4j.data.model.range.Range;

import java.time.LocalDateTime;
import java.util.Objects;

public record LocalDateTimeRange(
  Range<LocalDateTime> value
) {
  public LocalDateTimeRange {
    value = Objects.requireNonNull(value, "value");
  }

  public static @NotNull LocalDateTimeRange unbounded() {
    return new LocalDateTimeRange(Range.unbounded());
  }

  public static @NotNull LocalDateTimeRange closed(@NotNull LocalDateTime lower, @NotNull LocalDateTime upper) {
    return new LocalDateTimeRange(Range.closed(lower, upper));
  }

  public static @NotNull LocalDateTimeRange open(@NotNull LocalDateTime lower, @NotNull LocalDateTime upper) {
    return new LocalDateTimeRange(Range.open(lower, upper));
  }

  public static @NotNull LocalDateTimeRange closedOpen(@NotNull LocalDateTime lower, @NotNull LocalDateTime upper) {
    return new LocalDateTimeRange(Range.closedOpen(lower, upper));
  }

  public static @NotNull LocalDateTimeRange openClosed(@NotNull LocalDateTime lower, @NotNull LocalDateTime upper) {
    return new LocalDateTimeRange(Range.openClosed(lower, upper));
  }

  public static @NotNull LocalDateTimeRange atLeast(@NotNull LocalDateTime lower) {
    return new LocalDateTimeRange(Range.atLeast(lower));
  }

  public static @NotNull LocalDateTimeRange greaterThan(@NotNull LocalDateTime lower) {
    return new LocalDateTimeRange(Range.greaterThan(lower));
  }

  public static @NotNull LocalDateTimeRange atMost(@NotNull LocalDateTime upper) {
    return new LocalDateTimeRange(Range.atMost(upper));
  }

  public static @NotNull LocalDateTimeRange lessThan(@NotNull LocalDateTime upper) {
    return new LocalDateTimeRange(Range.lessThan(upper));
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

  public boolean contains(@Nullable LocalDateTime dateTime) {
    return value.contains(dateTime);
  }
}
