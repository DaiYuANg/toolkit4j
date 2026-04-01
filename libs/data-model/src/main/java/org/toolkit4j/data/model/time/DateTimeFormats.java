package org.toolkit4j.data.model.time;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.toolkit4j.data.model.enumeration.EnumValues;

@UtilityClass
public class DateTimeFormats {
  public final DateTimeFormatter ISO_LOCAL_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
  public final DateTimeFormatter ISO_OFFSET_DATE_TIME = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

  public @NotNull DateTimeFormatter of(@NotNull DateTimePattern pattern) {
    return Objects.requireNonNull(pattern, "pattern").formatter();
  }

  public @NotNull DateTimeFormatter ofPattern(@NotNull String pattern) {
    val normalizedPattern = Objects.requireNonNull(pattern, "pattern");
    return EnumValues.lookup(DateTimePattern.class)
        .findByPrimaryValue(normalizedPattern)
        .map(DateTimePattern::formatter)
        .orElseGet(() -> DateTimeFormatter.ofPattern(normalizedPattern));
  }
}
