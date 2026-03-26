package org.toolkit4j.data.model.time;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InstantRangeTest {

  @Test
  void closedRange_containsEndpoints() {
    var start = Instant.parse("2026-03-26T00:00:00Z");
    var end = Instant.parse("2026-03-26T01:00:00Z");
    var range = InstantRange.closed(start, end);

    assertTrue(range.contains(start));
    assertTrue(range.contains(Instant.parse("2026-03-26T00:30:00Z")));
    assertTrue(range.contains(end));
  }

  @Test
  void openRange_excludesEndpoints() {
    var start = Instant.parse("2026-03-26T00:00:00Z");
    var end = Instant.parse("2026-03-26T01:00:00Z");
    var range = InstantRange.open(start, end);

    assertFalse(range.contains(start));
    assertTrue(range.contains(Instant.parse("2026-03-26T00:30:00Z")));
    assertFalse(range.contains(end));
  }

  @Test
  void unboundedRange_containsAnyNonNullInstant() {
    var range = InstantRange.unbounded();

    assertTrue(range.contains(Instant.parse("2026-03-26T00:00:00Z")));
    assertFalse(range.contains(null));
  }
}
