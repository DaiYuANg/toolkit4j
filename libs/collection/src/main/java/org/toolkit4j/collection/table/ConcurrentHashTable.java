package org.toolkit4j.collection.table;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashTable<R, C, V> extends AbstractTable<R, C, V> {
  private final Map<R, Map<C, V>> backingMap = new ConcurrentHashMap<>();

  @Override
  protected Map<R, Map<C, V>> getBackingMap() {
    return backingMap;
  }

  @Override
  protected Map<C, V> createColumnMap() {
    return new ConcurrentHashMap<>();
  }

  @Override
  protected <V2> AbstractTable<R, C, V2> createInstance() {
    return new ConcurrentHashTable<>();
  }
}
