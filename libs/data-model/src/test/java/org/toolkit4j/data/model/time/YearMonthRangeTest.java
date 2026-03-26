package org.toolkit4j.data.model.time;

import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class YearMonthRangeTest {

  @Test
  void closedRange_acceptsBoundaryMonths() {
    var range = YearMonthRange.closed(
      YearMonthValue.of(2026, 1),
      YearMonthValue.of(2026, 3)
    );

    assertTrue(range.contains(YearMonthValue.of(2026, 1)));
    assertTrue(range.contains(YearMonth.of(2026, 2)));
    assertTrue(range.contains(YearMonthValue.of(2026, 3)));
  }

  @Test
  void openRange_excludesBoundaryMonths() {
    var range = YearMonthRange.open(
      YearMonthValue.of(2026, 1),
      YearMonthValue.of(2026, 3)
    );

    assertFalse(range.contains(YearMonth.of(2026, 1)));
    assertTrue(range.contains(YearMonth.of(2026, 2)));
    assertFalse(range.contains(YearMonth.of(2026, 3)));
  }
}
