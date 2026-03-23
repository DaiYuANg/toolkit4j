package org.toolkit4j.collection.table;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public interface Table<R, C, V> {

  V get(R rowKey, C columnKey);

  void put(R rowKey, C columnKey, V value);

  V remove(R rowKey, C columnKey);

  boolean contains(R rowKey, C columnKey);

  boolean containsRow(R rowKey);

  boolean containsColumn(C columnKey);

  Map<C, V> row(R rowKey);

  Map<R, V> column(C columnKey);

  Map<R, Map<C, V>> rowMap();

  Set<Cell<R, C, V>> cellSet();

  void clear();

  boolean isEmpty();

  int size();

  // --- Stream 扩展 / Stream extensions ---

  /**
   * 转成 Stream&lt;Cell&gt;。Returns stream of all cells.
   */
  default Stream<Cell<R, C, V>> stream() {
    return cellSet().stream();
  }

  /**
   * 所有行 key 的流（不重复）。Stream of distinct row keys.
   */
  default Stream<R> rowKeyStream() {
    return rowMap().keySet().stream();
  }

  /**
   * 所有列 key 的流（不重复）。Stream of distinct column keys.
   */
  default Stream<C> columnKeyStream() {
    return stream().map(Cell::getColumnKey).distinct();
  }

  /**
   * 所有值的流。Stream of all values.
   */
  default Stream<V> valueStream() {
    return stream().map(Cell::getValue);
  }

  /**
   * 按行遍历，每行 (rowKey, rowMap)。Stream of row entries.
   */
  default Stream<Map.Entry<R, Map<C, V>>> rowEntryStream() {
    return rowMap().entrySet().stream();
  }

  // --- 便捷方法 / Convenience methods ---

  /**
   * 获取值，不存在时返回默认值。Gets value or defaultValue if absent.
   */
  default V getOrDefault(R rowKey, C columnKey, V defaultValue) {
    V v = get(rowKey, columnKey);
    return v != null ? v : defaultValue;
  }

  /**
   * 批量放入另一个 Table 的所有单元格。Puts all cells from another table.
   */
  default void putAll(Table<? extends R, ? extends C, ? extends V> other) {
    other.stream().forEach(cell -> put(cell.getRowKey(), cell.getColumnKey(), cell.getValue()));
  }

  // --- 函数式扩展 / Functional extensions ---

  /**
   * 遍历每个单元格。Iterates over each cell.
   */
  default void forEach(BiConsumer<? super Cell<R, C, V>, ? super V> action) {
    cellSet().forEach(cell -> action.accept(cell, cell.getValue()));
  }

  /**
   * 条件过滤成新的 Table。Filters cells into a new table.
   */
  Table<R, C, V> filter(BiPredicate<R, C> predicate);

  /**
   * 把值映射成另一种类型。Maps values to a new table.
   */
  <V2> Table<R, C, V2> mapValues(Function<? super V, ? extends V2> mapper);
}
