package org.toolkit4j.core.reflect;

import org.junit.jupiter.api.Test;

class FieldVisitorTest {

  @Test
  void visitObject() {
    FieldVisitor.builder()
      .fieldProcessor((fieldInfo) -> {
      })
      .build();
  }
}