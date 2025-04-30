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

  // --- 函数式扩展 ---

  /**
   * 遍历每个单元格
   */
  default void forEach(BiConsumer<? super Cell<R, C, V>, ? super V> action) {
    cellSet().forEach(cell -> action.accept(cell, cell.getValue()));
  }

  /**
   * 条件过滤成新的 Table（惰性或 eager 可选）
   */
  Table<R, C, V> filter(BiPredicate<R, C> predicate);

  /**
   * 把值映射成另一种类型的 Table
   */
  <V2> Table<R, C, V2> mapValues(Function<? super V, ? extends V2> mapper);

  /**
   * 转成 Stream<Cell>，方便做 stream 流式处理
   */
  default Stream<Cell<R, C, V>> stream() {
    return cellSet().stream();
  }
}
