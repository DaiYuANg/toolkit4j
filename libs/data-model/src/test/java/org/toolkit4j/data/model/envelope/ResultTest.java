package org.toolkit4j.data.model.envelope;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ResultTest {

  @Test
  void factoryMethods_createExpectedShape() {
    var ok = Result.of(0, "ok", Map.of("id", 1L));
    var messageOnly = Result.<Integer, Object>ofMessage("created");

    assertEquals(0, ok.getCode());
    assertEquals("ok", ok.getMessage());
    assertEquals(1L, ok.getData().get("id"));
    assertNull(messageOnly.getCode());
    assertEquals("created", messageOnly.getMessage());
    assertNull(messageOnly.getData());
  }

  @Test
  void copyMethods_preserveOtherFields() {
    var result = Result.of(200, "ok", "payload");

    var withoutData = result.withoutData();
    var newData = result.withData(123L);
    var newCode = result.withCode("SUCCESS");
    var newMessage = result.withMessage("updated");

    assertNull(withoutData.getData());
    assertEquals(123L, newData.getData());
    assertEquals("SUCCESS", newCode.getCode());
    assertEquals("payload", newCode.getData());
    assertEquals("updated", newMessage.getMessage());
    assertEquals(200, newMessage.getCode());
  }
}
