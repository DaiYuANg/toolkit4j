package org.toolkit4j.collection.table;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HashTableTest {

  private Table<String, String, Integer> table;

  @BeforeEach
  void setUp() {
    table = new HashTable<>();
  }

  @Test
  void testPutAndGet() {
    table.put("r1", "c1", 100);
    table.put("r1", "c2", 200);
    table.put("r2", "c1", 300);

    assertEquals(100, table.get("r1", "c1"));
    assertEquals(200, table.get("r1", "c2"));
    assertEquals(300, table.get("r2", "c1"));
    assertNull(table.get("r3", "c1")); // 不存在的行列
  }

  @Test
  void testRemove() {
    table.put("r1", "c1", 100);
    table.put("r1", "c2", 200);

    Integer removed = table.remove("r1", "c1");
    assertEquals(100, removed);
    assertNull(table.get("r1", "c1"));
    assertEquals(1, table.size());

    // 删除最后一个列后，行应被移除
    table.remove("r1", "c2");
    assertFalse(table.containsRow("r1"));
  }

  @Test
  void testContains() {
    table.put("r1", "c1", 100);
    assertTrue(table.contains("r1", "c1"));
    assertFalse(table.contains("r1", "c2"));
    assertFalse(table.contains("r2", "c1"));
    assertTrue(table.containsRow("r1"));
    assertFalse(table.containsRow("r2"));
    assertTrue(table.containsColumn("c1"));
    assertFalse(table.containsColumn("c2"));
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
    assertEquals(100, columnC1.get("r1"));
    assertEquals(300, columnC1.get("r2"));
  }

  @Test
  void testCellSet() {
    table.put("r1", "c1", 100);
    table.put("r2", "c2", 200);

    Set<Cell<String, String, Integer>> cells = table.cellSet();
    assertEquals(2, cells.size());

    boolean foundR1C1 =
        cells.stream()
            .anyMatch(
                cell ->
                    cell.getRowKey().equals("r1")
                        && cell.getColumnKey().equals("c1")
                        && cell.getValue().equals(100));
    assertTrue(foundR1C1);

    boolean foundR2C2 =
        cells.stream()
            .anyMatch(
                cell ->
                    cell.getRowKey().equals("r2")
                        && cell.getColumnKey().equals("c2")
                        && cell.getValue().equals(200));
    assertTrue(foundR2C2);
  }

  @Test
  void testClearAndIsEmpty() {
    assertTrue(table.isEmpty());
    table.put("r1", "c1", 100);
    assertFalse(table.isEmpty());

    table.clear();
    assertTrue(table.isEmpty());
  }

  @Test
  void testSize() {
    assertEquals(0, table.size());
    table.put("r1", "c1", 100);
    table.put("r1", "c2", 200);
    assertEquals(2, table.size());
    table.remove("r1", "c1");
    assertEquals(1, table.size());
  }

  @Test
  void testFilter() {
    table.put("r1", "c1", 100);
    table.put("r1", "c2", 200);
    table.put("r2", "c1", 300);

    Table<String, String, Integer> filtered = table.filter((r, c) -> "r1".equals(r));
    assertEquals(2, filtered.size());
    assertTrue(filtered.contains("r1", "c1"));
    assertFalse(filtered.contains("r2", "c1"));
  }

  @Test
  void testMapValues() {
    table.put("r1", "c1", 100);
    table.put("r1", "c2", 200);

    Table<String, String, String> mapped = table.mapValues(Object::toString);
    assertEquals("100", mapped.get("r1", "c1"));
    assertEquals("200", mapped.get("r1", "c2"));
  }

  @Test
  void testStream() {
    table.put("r1", "c1", 100);
    table.put("r2", "c2", 200);

    long count = table.stream().count();
    assertEquals(2, count);
  }

  @Test
  void testStreamExtensions() {
    table.put("r1", "c1", 100);
    table.put("r1", "c2", 200);
    table.put("r2", "c1", 300);

    val rowKeys = table.rowKeyStream().toList();
    assertEquals(2, rowKeys.size());
    assertTrue(rowKeys.containsAll(List.of("r1", "r2")));

    val colKeys = table.columnKeyStream().toList();
    assertEquals(2, colKeys.size());
    assertTrue(colKeys.containsAll(List.of("c1", "c2")));

    val values = table.valueStream().toList();
    assertEquals(3, values.size());
    assertTrue(values.containsAll(List.of(100, 200, 300)));
  }

  @Test
  void testGetOrDefault() {
    table.put("r1", "c1", 100);
    assertEquals(100, table.getOrDefault("r1", "c1", -1));
    assertEquals(-1, table.getOrDefault("r1", "c2", -1));
    assertEquals(-1, table.getOrDefault("r2", "c1", -1));
  }

  @Test
  void testPutAll() {
    table.put("r1", "c1", 100);
    val other = new HashTable<String, String, Integer>();
    other.put("r1", "c2", 200);
    other.put("r2", "c1", 300);

    table.putAll(other);
    assertEquals(3, table.size());
    assertEquals(100, table.get("r1", "c1"));
    assertEquals(200, table.get("r1", "c2"));
    assertEquals(300, table.get("r2", "c1"));
  }

  // --- filter / mapValues 边界情况 ---
  @Test
  void testFilter_onEmptyTable() {
    Table<String, String, Integer> filtered = table.filter((r, c) -> true);
    assertTrue(filtered.isEmpty());
  }

  @Test
  void testFilter_allExcluded() {
    table.put("r1", "c1", 100);
    table.put("r2", "c2", 200);
    Table<String, String, Integer> filtered = table.filter((r, c) -> false);
    assertTrue(filtered.isEmpty());
  }

  @Test
  void testFilter_allIncluded() {
    table.put("r1", "c1", 100);
    table.put("r2", "c2", 200);
    Table<String, String, Integer> filtered = table.filter((r, c) -> true);
    assertEquals(2, filtered.size());
  }

  @Test
  void testMapValues_onEmptyTable() {
    Table<String, String, String> mapped = table.mapValues(Object::toString);
    assertTrue(mapped.isEmpty());
  }

  @Test
  void testMapValues_mapperReturnsNull() {
    table.put("r1", "c1", 100);
    table.put("r1", "c2", 200);
    Table<String, String, Integer> mapped = table.mapValues(v -> v > 150 ? null : v);
    assertEquals(100, mapped.get("r1", "c1"));
    assertNull(mapped.get("r1", "c2"));
  }
}
