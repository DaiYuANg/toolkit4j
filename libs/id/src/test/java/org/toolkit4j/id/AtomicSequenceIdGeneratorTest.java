package org.toolkit4j.id;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AtomicSequenceIdGeneratorTest {
  @Test
  void testInitialValueDefault() {
    AtomicSequenceIdGenerator generator = new AtomicSequenceIdGenerator();
    assertEquals(0L, generator.nextId());
    assertEquals(1L, generator.nextId());
    assertEquals(2L, generator.nextId());
  }

  @Test
  void testInitialValueCustom() {
    AtomicSequenceIdGenerator generator = new AtomicSequenceIdGenerator(100L);
    assertEquals(100L, generator.nextId());
    assertEquals(101L, generator.nextId());
  }

  @Test
  void testReset() {
    AtomicSequenceIdGenerator generator = new AtomicSequenceIdGenerator(10L);
    assertEquals(10L, generator.nextId());
    assertEquals(11L, generator.nextId());

    generator.reset(50L);
    assertEquals(50L, generator.nextId());
    assertEquals(51L, generator.nextId());
  }

  @Test
  void testThreadSafety() throws InterruptedException {
    AtomicSequenceIdGenerator generator = new AtomicSequenceIdGenerator(0L);

    final int threads = 10;
    final int idsPerThread = 1000;
    Thread[] threadArray = new Thread[threads];
    long[] results = new long[threads * idsPerThread];

    for (int i = 0; i < threads; i++) {
      final int threadIndex = i;
      threadArray[i] = new Thread(() -> {
        for (int j = 0; j < idsPerThread; j++) {
          long id = generator.nextId();
          results[threadIndex * idsPerThread + j] = id;
        }
      });
      threadArray[i].start();
    }

    for (Thread t : threadArray) {
      t.join();
    }

    // 验证结果长度和无重复
    assertEquals(threads * idsPerThread, results.length);

    // 使用 Set 验证无重复
    long distinctCount = java.util.Arrays.stream(results).distinct().count();
    assertEquals(results.length, distinctCount);
  }
}