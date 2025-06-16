package org.toolkit4j.id;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UUIDUtilTest {

  @Test
  void testRandom() {
    val uuid = UUIDUtil.random();
    assertNotNull(uuid);
    // 标准UUID字符串长度36，包含4个'-'
    assertEquals(36, uuid.length());
    assertEquals(4, uuid.chars().filter(ch -> ch == '-').count());
  }

  @Test
  void testFast() {
    val uuid = UUIDUtil.fast();
    assertNotNull(uuid);
    // fast() 生成无 '-' 的32位16进制字符串
    assertEquals(32, uuid.length());
    assertTrue(uuid.matches("[0-9a-f]{32}"), "UUID must be lowercase hex");
  }

  @Test
  void testUniqueness() {
    // 简单测试生成10000个 fast UUID 是否唯一
    val count = 10_000;
    val set = new HashSet<String>(count);
    IntStream.range(0, count).mapToObj(i -> UUIDUtil.fast()).forEach(id -> {
      log.atInfo().log(id);
      assertFalse(set.contains(id), "Duplicate UUID generated: " + id);
      set.add(id);
    });
  }
}