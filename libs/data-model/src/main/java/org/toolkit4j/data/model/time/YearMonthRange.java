package org.toolkit4j.data.model.time;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.toolkit4j.data.model.range.Range;

import java.time.YearMonth;
import java.util.Objects;

public record YearMonthRange(
  Range<YearMonthValue> value
) {
  public YearMonthRange {
    value = Objects.requireNonNull(value, "value");
  }

  public static @NotNull YearMonthRange unbounded() {
    return new YearMonthRange(Range.unbounded());
  }

  public static @NotNull YearMonthRange closed(@NotNull YearMonthValue lower, @NotNull YearMonthValue upper) {
    return new YearMonthRange(Range.closed(lower, upper));
  }

  public static @NotNull YearMonthRange closed(@NotNull YearMonth lower, @NotNull YearMonth upper) {
    return closed(YearMonthValue.from(lower), YearMonthValue.from(upper));
  }

  public static @NotNull YearMonthRange open(@NotNull YearMonthValue lower, @NotNull YearMonthValue upper) {
    return new YearMonthRange(Range.open(lower, upper));
  }

  public static @NotNull YearMonthRange closedOpen(@NotNull YearMonthValue lower, @NotNull YearMonthValue upper) {
    return new YearMonthRange(Range.closedOpen(lower, upper));
  }

  public static @NotNull YearMonthRange openClosed(@NotNull YearMonthValue lower, @NotNull YearMonthValue upper) {
    return new YearMonthRange(Range.openClosed(lower, upper));
  }

  public static @NotNull YearMonthRange atLeast(@NotNull YearMonthValue lower) {
    return new YearMonthRange(Range.atLeast(lower));
  }

  public static @NotNull YearMonthRange greaterThan(@NotNull YearMonthValue lower) {
    return new YearMonthRange(Range.greaterThan(lower));
  }

  public static @NotNull YearMonthRange atMost(@NotNull YearMonthValue upper) {
    return new YearMonthRange(Range.atMost(upper));
  }

  public static @NotNull YearMonthRange lessThan(@NotNull YearMonthValue upper) {
    return new YearMonthRange(Range.lessThan(upper));
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

  public boolean contains(@Nullable YearMonthValue yearMonth) {
    return value.contains(yearMonth);
  }

  public boolean contains(@Nullable YearMonth yearMonth) {
    return yearMonth != null && contains(YearMonthValue.from(yearMonth));
  }
}
