package org.toolkit4j.data.model.time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;

class YearMonthValueTest {

  @Test
  void parseAndFormat_useIsoYearMonthSemantics() {
    var value = YearMonthValue.parse("2026-03");

    assertEquals(2026, value.year());
    assertEquals(3, value.month());
    assertEquals("2026-03", value.toString());
    assertEquals("202603", value.format(DateTimeFormatter.ofPattern("yyyyMM")));
  }

  @Test
  void conversionHelpers_exposeMonthDates() {
    var value = YearMonthValue.of(2026, 2);

    assertEquals(LocalDate.of(2026, 2, 1), value.atDay(1));
    assertEquals(LocalDate.of(2026, 2, 28), value.atEndOfMonth());
    assertEquals(YearMonth.of(2026, 2), value.toYearMonth());
  }

  @Test
  void compareTo_ordersByYearAndMonth() {
    assertTrue(YearMonthValue.of(2026, 2).compareTo(YearMonthValue.of(2026, 3)) < 0);
  }

  @Test
  void invalidMonth_isRejected() {
    assertThrows(RuntimeException.class, () -> YearMonthValue.of(2026, 13));
  }
}
