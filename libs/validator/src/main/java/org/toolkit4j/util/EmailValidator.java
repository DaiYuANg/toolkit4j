package org.toolkit4j.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailValidator {
  public final String EMAIL_REGEX =
    "^[a-zA-Z0-9_+&*-]+(?:\\." +
      "[a-zA-Z0-9_+&*-]+)*@" +
      "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
      "A-Z]{2,7}$";

  public final java.util.regex.Pattern EMAIL_PATTERN =
    java.util.regex.Pattern.compile(EMAIL_REGEX);

  public static boolean isEmail(@NonNull String email) {
    return EMAIL_PATTERN.matcher(email).matches();
  }
}
