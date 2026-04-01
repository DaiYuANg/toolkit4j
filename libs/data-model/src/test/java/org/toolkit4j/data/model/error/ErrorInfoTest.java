package org.toolkit4j.data.model.error;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class ErrorInfoTest {

  @Test
  void of_createsErrorWithoutDetails() {
    var error = ErrorInfo.of(SampleErrorCode.INVALID_INPUT, "invalid input");

    assertEquals(SampleErrorCode.INVALID_INPUT, error.getCode());
    assertFalse(error.hasDetails());
  }

  @Test
  void details_areImmutableCopies() {
    var error =
        new ErrorInfo<>(
            SampleErrorCode.INVALID_INPUT,
            "invalid input",
            List.of(new FieldError("name", "blank", "must not be blank", null)));

    assertTrue(error.hasDetails());
    assertEquals(1, error.getDetails().size());
    assertThrows(
        UnsupportedOperationException.class,
        () -> error.getDetails().add(new GeneralErrorDetail("x", "y", "z")));
  }

  @Test
  void withDetail_appendsNewDetail() {
    var error =
        ErrorInfo.of(SampleErrorCode.INVALID_INPUT, "invalid input")
            .withDetail(new FieldError("name", "blank", "must not be blank", null));

    assertTrue(error.hasDetails());
    assertEquals(1, error.detailCount());
  }

  private enum SampleErrorCode implements ErrorCode {
    INVALID_INPUT;

    @Override
    public String getPrimaryValue() {
      return name();
    }
  }
}
