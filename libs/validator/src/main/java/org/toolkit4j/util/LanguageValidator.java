package org.toolkit4j.util;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

@UtilityClass
public class LanguageValidator {

  public final String pattern = "[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]";

  /**
   * 校验字符串是否只包含中文字符
   *
   * @param value 待校验字符串
   * @return true 如果全部是中文，false 否则
   */
  public boolean validateChinese(String value) {
    if (StringUtils.isEmpty(value)) {
      throw new IllegalArgumentException("value is empty!");
    }
    val p = Pattern.compile(pattern);
    val m = p.matcher(value);
    return m.find();
  }
}
