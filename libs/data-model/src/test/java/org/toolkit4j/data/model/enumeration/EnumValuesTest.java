package org.toolkit4j.data.model.enumeration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EnumValuesTest {

  @Test
  void lookup_resolvesEnumByPrimaryValue() {
    var lookup = EnumValues.lookup(Status.class);

    assertSame(Status.class, lookup.enumType());
    assertSame(Status.ENABLED, lookup.fromPrimaryValue("enabled"));
    assertTrue(lookup.containsPrimaryValue("disabled"));
    assertEquals(2, lookup.primaryValues().size());
  }

  @Test
  void lookup_isCachedPerEnumType() {
    var first = EnumValues.lookup(Status.class);
    var second = EnumValues.lookup(Status.class);

    assertSame(first, second);
  }

  @Test
  void lookup_rejectsDuplicatePrimaryValues() {
    assertThrows(IllegalArgumentException.class, () -> EnumValues.lookup(DuplicateStatus.class));
  }

  @Test
  void lookup_handlesNullQueriesForOptionalAccessors() {
    var lookup = EnumValues.lookup(Status.class);

    assertTrue(lookup.findByPrimaryValue(null).isEmpty());
    assertTrue(!lookup.containsPrimaryValue(null));
  }

  private enum Status implements EnumValue<String> {
    ENABLED("enabled"),
    DISABLED("disabled");

    private final String primaryValue;

    Status(String primaryValue) {
      this.primaryValue = primaryValue;
    }

    @Override
    public String getPrimaryValue() {
      return primaryValue;
    }
  }

  private enum DuplicateStatus implements EnumValue<String> {
    FIRST("same"),
    SECOND("same");

    private final String primaryValue;

    DuplicateStatus(String primaryValue) {
      this.primaryValue = primaryValue;
    }

    @Override
    public String getPrimaryValue() {
      return primaryValue;
    }
  }
}
