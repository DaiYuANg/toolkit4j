package org.toolkit4j.collection.table;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record RecordCell<R, C, V>(
  R rowKey,
  C columnKey,
  V value
) implements Cell<R, C, V> {
  @Override
  public R getRowKey() {
    return rowKey;
  }

  @Override
  public C getColumnKey() {
    return columnKey;
  }

  @Override
  public V getValue() {
    return value;
  }
}
