package org.toolkit4j.data.model.range;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RangeTest {

  @Test
  void closedRange_containsEndpoints() {
    var range = Range.closed(1, 3);

    assertTrue(range.contains(1));
    assertTrue(range.contains(2));
    assertTrue(range.contains(3));
    assertFalse(range.contains(4));
  }

  @Test
  void openRange_excludesEndpoints() {
    var range = Range.open(1, 3);

    assertFalse(range.contains(1));
    assertTrue(range.contains(2));
    assertFalse(range.contains(3));
  }

  @Test
  void equalOpenBounds_areEmpty() {
    var range = Range.open(5, 5);

    assertTrue(range.isEmpty());
    assertFalse(range.contains(5));
  }

  @Test
  void lowerBoundGreaterThanUpperBound_isRejected() {
    assertThrows(IllegalArgumentException.class, () -> Range.closed(5, 1));
  }

  @Test
  void unboundedRange_containsAnyNonNullValue() {
    var range = Range.<Integer>unbounded();

    assertTrue(range.contains(-1));
    assertTrue(range.contains(100));
    assertFalse(range.contains(null));
  }
}
