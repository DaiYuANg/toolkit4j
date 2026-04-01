package org.toolkit4j.collection.table;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
class ConcurrentHashTableTest {

  private Table<String, String, Integer> table;

  @BeforeEach
  void setUp() {
    table = new ConcurrentHashTable<>();
  }

  @Test
  void testPutAndGet() {
    table.put("r1", "c1", 100);
    table.put("r1", "c2", 200);
    table.put("r2", "c1", 300);

    assertEquals(100, table.get("r1", "c1"));
    assertEquals(200, table.get("r1", "c2"));
    assertEquals(300, table.get("r2", "c1"));
    assertNull(table.get("r3", "c1"));
  }

  @Test
  void testRemove() {
    table.put("r1", "c1", 100);
    table.put("r1", "c2", 200);

    Integer removed = table.remove("r1", "c1");
    assertEquals(100, removed);
    assertNull(table.get("r1", "c1"));
    assertEquals(1, table.size());

    table.remove("r1", "c2");
    assertFalse(table.containsRow("r1"));
  }

  @Test
  void testContains() {
    table.put("r1", "c1", 100);
    assertTrue(table.contains("r1", "c1"));
    assertFalse(table.contains("r1", "c2"));
    assertTrue(table.containsRow("r1"));
    assertTrue(table.containsColumn("c1"));
  }

  @Test
  void testRowAndColumnView() {
    table.put("r1", "c1", 100);
    table.put("r1", "c2", 200);
    table.put("r2", "c1", 300);

    Map<String, Integer> row1 = table.row("r1");
    assertEquals(2, row1.size());
    assertEquals(100, row1.get("c1"));
    assertEquals(200, row1.get("c2"));

    Map<String, Integer> columnC1 = table.column("c1");
    assertEquals(2, columnC1.size());
  }

  @Test
  void testConcurrentPut() throws InterruptedException {
    val threads = 4;
    val perThread = 500;
    val latch = new CountDownLatch(1);
    val executor = Executors.newFixedThreadPool(threads);

    for (int t = 0; t < threads; t++) {
      val tid = t;
      executor.submit(
          () -> {
            try {
              latch.await();
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            }
            for (int i = 0; i < perThread; i++) {
              val r = "r" + (tid * 1000 + i) % 100;
              val c = "c" + (tid * 1000 + i);
              table.put(r, c, tid * 1000 + i);
            }
          });
    }

    latch.countDown();
    executor.shutdown();
    assertTrue(executor.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS));

    // 验证各线程写入的数据可正确读出，且 size 正确（已用 AtomicInteger 修复）
    assertEquals(threads * perThread, table.size());
    for (int tid = 0; tid < threads; tid++) {
      for (int i = 0; i < 10; i++) {
        val r = "r" + ((tid * 1000 + i) % 100);
        val c = "c" + (tid * 1000 + i);
        assertEquals(tid * 1000 + i, table.get(r, c), "Concurrent put should be readable");
      }
    }
  }

  @Test
  void testConcurrentPutAndGet() throws InterruptedException {
    val putThreads = 2;
    val getThreads = 2;
    val putCount = 1000;
    val executor = Executors.newFixedThreadPool(putThreads + getThreads);
    val errors = new AtomicInteger(0);

    IntStream.range(0, putCount).forEach(i -> table.put("r" + (i % 50), "c" + i, i));

    val latch = new CountDownLatch(1);
    for (int t = 0; t < putThreads; t++) {
      val tid = t;
      executor.submit(
          () -> {
            try {
              latch.await();
              for (int i = 0; i < 200; i++) {
                table.put("r_new_" + tid, "c" + i, i);
              }
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              errors.incrementAndGet();
            }
          });
    }

    for (int t = 0; t < getThreads; t++) {
      executor.submit(
          () -> {
            try {
              latch.await();
              for (int i = 0; i < putCount; i++) {
                val v = table.get("r" + (i % 50), "c" + i);
                if (v != null && !v.equals(i)) errors.incrementAndGet();
              }
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              errors.incrementAndGet();
            }
          });
    }

    latch.countDown();
    executor.shutdown();
    assertTrue(executor.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS));
    assertEquals(0, errors.get());
  }

  @Test
  void testFilter() {
    table.put("r1", "c1", 100);
    table.put("r1", "c2", 200);
    table.put("r2", "c1", 300);

    Table<String, String, Integer> filtered = table.filter((r, c) -> "r1".equals(r));
    assertEquals(2, filtered.size());
  }

  @Test
  void testStreamExtensions() {
    table.put("r1", "c1", 100);
    table.put("r1", "c2", 200);
    table.put("r2", "c1", 300);

    val rowKeys = table.rowKeyStream().toList();
    assertEquals(2, rowKeys.size());
    assertTrue(rowKeys.containsAll(List.of("r1", "r2")));
  }
}
