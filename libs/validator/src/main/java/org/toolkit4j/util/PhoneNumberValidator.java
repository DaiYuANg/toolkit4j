package org.toolkit4j.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class PhoneNumberValidator {
  public final String PHONE_REGEX = "^1[3-9]\\d{9}$";

  /**
   * 校验手机号格式是否有效（国内手机号）
   *
   * @param value 待校验的手机号
   * @return true 合法，false 不合法
   */
  public static boolean isValid(@NonNull Object value) {
    val phone = value.toString().trim();
    return phone.matches(PHONE_REGEX);
  }
}
