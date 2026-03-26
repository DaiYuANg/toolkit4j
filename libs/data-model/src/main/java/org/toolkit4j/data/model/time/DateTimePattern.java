package org.toolkit4j.data.model.time;

import org.jetbrains.annotations.NotNull;
import org.toolkit4j.data.model.enumeration.EnumValue;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public enum DateTimePattern implements EnumValue<String> {
  STANDARD_DATE_TIME("yyyy-MM-dd HH:mm:ss"),
  YEAR_MONTH("yyyy-MM"),
  DATE("yyyy-MM-dd"),
  BASIC_YEAR_MONTH("yyyyMM"),
  BASIC_DATE("yyyyMMdd"),
  TIME("HH:mm:ss"),
  TIME_MILLIS("HH:mm:ss.SSS"),
  BASIC_TIMESTAMP("yyyyMMddHHmmss");

  private final String primaryValue;
  private final DateTimeFormatter formatter;

  DateTimePattern(String primaryValue) {
    this.primaryValue = Objects.requireNonNull(primaryValue, "primaryValue");
    this.formatter = DateTimeFormatter.ofPattern(primaryValue);
  }

  @Override
  public @NotNull String getPrimaryValue() {
    return primaryValue;
  }

  public @NotNull DateTimeFormatter formatter() {
    return formatter;
  }
}
