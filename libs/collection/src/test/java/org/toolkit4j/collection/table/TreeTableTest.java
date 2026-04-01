package org.toolkit4j.collection.table;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Comparator;
import lombok.val;
import org.junit.jupiter.api.Test;

class TreeTableTest {
  @Test
  void testPutAndGet() {
    val table = new TreeTable<String, String, Integer>();

    table.put("row1", "col1", 10);
    table.put("row1", "col2", 20);
    table.put("row2", "col1", 30);

    assertEquals(10, table.get("row1", "col1"));
    assertEquals(20, table.get("row1", "col2"));
    assertEquals(30, table.get("row2", "col1"));
    assertNull(table.get("row2", "col2"));
  }

  @Test
  void testRowOrdering() {
    val table = new TreeTable<Integer, String, String>();

    table.put(10, "c1", "v10");
    table.put(5, "c1", "v5");
    table.put(7, "c1", "v7");

    // rowMap 按行 key 自然排序
    Integer[] expectedOrder = {5, 7, 10};
    int index = 0;
    for (Integer rowKey : table.rowMap().keySet()) {
      assertEquals(expectedOrder[index++], rowKey);
    }
  }

  @Test
  void testCustomComparator() {
    // 按降序排序行 key
    val descComparator = Comparator.<Integer>reverseOrder();
    val table = new TreeTable<Integer, String, String>(descComparator);

    table.put(10, "c1", "v10");
    table.put(5, "c1", "v5");
    table.put(7, "c1", "v7");

    Integer[] expectedOrder = {10, 7, 5};
    int index = 0;
    for (Integer rowKey : table.rowMap().keySet()) {
      assertEquals(expectedOrder[index++], rowKey);
    }
  }

  @Test
  void testFilterPreservesComparator() {
    val descComparator = Comparator.<Integer>reverseOrder();
    val table = new TreeTable<Integer, String, String>(descComparator);
    table.put(10, "c1", "v10");
    table.put(5, "c1", "v5");
    table.put(7, "c1", "v7");

    Table<Integer, String, String> filtered = table.filter((r, c) -> r >= 7);
    assertEquals(2, filtered.size());
    // 验证 filter 后的 Table 行 key 仍为降序
    val rowKeys = filtered.rowMap().keySet().iterator();
    assertEquals(10, rowKeys.next());
    assertEquals(7, rowKeys.next());
  }
}
