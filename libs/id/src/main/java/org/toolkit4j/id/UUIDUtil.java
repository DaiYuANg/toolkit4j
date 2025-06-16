package org.toolkit4j.id;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Long.toHexString;

@UtilityClass
public class UUIDUtil {
  public String random() {
    return UUID.randomUUID().toString();
  }

  public String fast() {
    val random = ThreadLocalRandom.current();
    var mostSigBits = random.nextLong();
    var leastSigBits = random.nextLong();

    // 设置版本号为4 (UUID version 4)
    mostSigBits &= 0xffffffffffff0fffL; // 清除版本位
    mostSigBits |= 0x0000000000004000L; // 设置版本为4

    // 设置变体为IETF标准
    leastSigBits &= 0x3fffffffffffffffL; // 清除变体位
    leastSigBits |= 0x8000000000000000L; // 设置变体位

    return digits(mostSigBits >>> 32) +
      digits(mostSigBits & 0xffffffffL) +
      digits(leastSigBits >>> 32) +
      digits(leastSigBits & 0xffffffffL);
  }

  // 转成固定长度16进制字符串，不足高位补0
  private @NotNull String digits(long val) {
    val hi = 1L << (8 * 4);
    return toHexString(hi | (val & (hi - 1))).substring(1);
  }
}
