package org.toolkit4j.data.model.page;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PageResultTest {

  @Test
  void empty_usesFirstPageSemantics() {
    var empty = PageResult.<String>empty();

    assertEquals(1, empty.getPage());
    assertEquals(0, empty.getSize());
    assertTrue(empty.isEmpty());
  }

  @Test
  void normalized_fillsMissingFields() {
    var normalized = new PageResult<String>(null, null, null, null, null).normalized();

    assertEquals(1, normalized.getPage());
    assertEquals(0, normalized.getSize());
    assertEquals(0L, normalized.getTotalElements());
    assertEquals(0L, normalized.getTotalPages());
    assertTrue(normalized.getContent().isEmpty());
  }
}
