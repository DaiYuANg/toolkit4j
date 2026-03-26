package org.toolkit4j.data.model.value;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OptionTest {

  @Test
  void constructor_normalizesNullChildrenToEmptyList() {
    var option = new Option<>("value", "label", null, null);

    assertFalse(option.hasChildren());
    assertEquals(List.of(), option.children());
  }

  @Test
  void constructor_copiesChildrenAsUnmodifiableList() {
    var source = new ArrayList<>(List.of(Option.of("child", "Child")));
    var option = new Option<>("value", "label", null, source);
    source.clear();

    assertEquals(1, option.children().size());
    assertThrows(UnsupportedOperationException.class, () -> option.children().add(Option.of("x", "X")));
  }
}
