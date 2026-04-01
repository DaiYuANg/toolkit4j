package org.toolkit4j.data.model.time;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class LocalDateTimeRangeTest {

  @Test
  void closedOpenRange_excludesUpperBoundary() {
    var start = LocalDateTime.of(2026, 3, 26, 8, 0, 0);
    var end = LocalDateTime.of(2026, 3, 26, 9, 0, 0);
    var range = LocalDateTimeRange.closedOpen(start, end);

    assertTrue(range.contains(start));
    assertTrue(range.contains(LocalDateTime.of(2026, 3, 26, 8, 30, 0)));
    assertFalse(range.contains(end));
  }

  @Test
  void upperBoundOnlyRange_acceptsEarlierValues() {
    var end = LocalDateTime.of(2026, 3, 26, 9, 0, 0);
    var range = LocalDateTimeRange.atMost(end);

    assertTrue(range.hasUpperBound());
    assertFalse(range.hasLowerBound());
    assertTrue(range.contains(LocalDateTime.of(2026, 3, 26, 8, 59, 59)));
  }
}
