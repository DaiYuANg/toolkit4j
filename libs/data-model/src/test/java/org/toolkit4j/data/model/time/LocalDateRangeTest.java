package org.toolkit4j.data.model.time;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class LocalDateRangeTest {

  @Test
  void closedRange_containsBoundaryDates() {
    var start = LocalDate.of(2026, 3, 1);
    var end = LocalDate.of(2026, 3, 31);
    var range = LocalDateRange.closed(start, end);

    assertTrue(range.contains(start));
    assertTrue(range.contains(LocalDate.of(2026, 3, 15)));
    assertTrue(range.contains(end));
  }

  @Test
  void halfOpenRange_excludesUpperBoundary() {
    var start = LocalDate.of(2026, 3, 1);
    var end = LocalDate.of(2026, 4, 1);
    var range = LocalDateRange.closedOpen(start, end);

    assertTrue(range.contains(start));
    assertTrue(range.contains(LocalDate.of(2026, 3, 31)));
    assertFalse(range.contains(end));
  }

  @Test
  void lowerBoundOnlyRange_reportsItsShape() {
    var range = LocalDateRange.atLeast(LocalDate.of(2026, 3, 1));

    assertTrue(range.hasLowerBound());
    assertFalse(range.hasUpperBound());
    assertTrue(range.contains(LocalDate.of(2026, 4, 1)));
  }
}
