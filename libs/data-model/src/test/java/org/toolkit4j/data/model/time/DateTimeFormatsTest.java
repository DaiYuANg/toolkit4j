package org.toolkit4j.data.model.time;

import org.junit.jupiter.api.Test;
import org.toolkit4j.data.model.enumeration.EnumValues;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class DateTimeFormatsTest {

  @Test
  void commonPatternsExposeStablePrimaryValues() {
    assertEquals("yyyy-MM-dd HH:mm:ss", DateTimePattern.STANDARD_DATE_TIME.getPrimaryValue());
    assertEquals("yyyyMMdd", DateTimePattern.BASIC_DATE.getPrimaryValue());
    assertSame(DateTimePattern.DATE, EnumValues.lookup(DateTimePattern.class).fromPrimaryValue("yyyy-MM-dd"));
  }

  @Test
  void knownPatternResolvesFormatterFromEnumPreset() {
    var value = LocalDateTime.of(2026, 3, 26, 8, 9, 10);

    assertEquals("2026-03-26 08:09:10", DateTimeFormats.of(DateTimePattern.STANDARD_DATE_TIME).format(value));
    assertEquals("20260326", DateTimeFormats.ofPattern("yyyyMMdd").format(value));
  }

  @Test
  void isoFormattersRemainAvailableThroughUtilityClass() {
    var localDateTime = LocalDateTime.of(2026, 3, 26, 8, 9, 10);
    var offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.ofHours(8));

    assertEquals("2026-03-26T08:09:10", DateTimeFormats.ISO_LOCAL_DATE_TIME.format(localDateTime));
    assertEquals("2026-03-26T08:09:10+08:00", DateTimeFormats.ISO_OFFSET_DATE_TIME.format(offsetDateTime));
  }
}
