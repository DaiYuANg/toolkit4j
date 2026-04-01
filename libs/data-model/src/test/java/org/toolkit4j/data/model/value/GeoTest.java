package org.toolkit4j.data.model.value;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GeoTest {

  @Test
  void isValid_acceptsNegativeCoordinatesWithinRange() {
    var geo = new Geo(-73.9857, 40.7484);

    assertTrue(geo.isComplete());
    assertTrue(geo.isValid());
  }

  @Test
  void isValid_rejectsOutOfRangeCoordinates() {
    var geo = new Geo(181.0, 95.0);

    assertFalse(geo.isValid());
  }
}
