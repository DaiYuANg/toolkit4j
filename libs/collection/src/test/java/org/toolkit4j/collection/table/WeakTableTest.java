package org.toolkit4j.collection.table;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * WeakTable 基础行为测试。弱引用键的回收行为依赖 GC，不易稳定复现，此处仅验证 Table 契约。
 */
class WeakTableTest {

  private Table<String, String, Integer> table;

  @BeforeEach
  void setUp() {
    table = new WeakTable<>();
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

    assertEquals(100, table.remove("r1", "c1"));
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

    Map<String, Integer> columnC1 = table.column("c1");
    assertEquals(2, columnC1.size());
  }

  @Test
  void testClearAndIsEmpty() {
    table.put("r1", "c1", 100);
    assertFalse(table.isEmpty());
    table.clear();
    assertTrue(table.isEmpty());
    assertNull(table.get("r1", "c1"));
  }

  @Test
  void testFilter() {
    table.put("r1", "c1", 100);
    table.put("r2", "c1", 200);
    Table<String, String, Integer> filtered = table.filter((r, c) -> "r1".equals(r));
    assertEquals(1, filtered.size());
    assertEquals(100, filtered.get("r1", "c1"));
  }

  @Test
  void testMapValues() {
    table.put("r1", "c1", 100);
    Table<String, String, String> mapped = table.mapValues(Object::toString);
    assertEquals("100", mapped.get("r1", "c1"));
  }
}
