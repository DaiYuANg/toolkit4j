package org.toolkit4j.sensitive;

import cn.hutool.core.util.DesensitizedUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SensitiveUtils {
  public String desensitize(String value, Sensitive.Type type) {
    if (value == null) return null;
    return switch (type) {
      case MOBILE -> DesensitizedUtil.mobilePhone(value);
      case EMAIL -> DesensitizedUtil.email(value);
      case ID_CARD -> DesensitizedUtil.idCardNum(value, 1, 0);
      default -> value;
    };
  }
}
