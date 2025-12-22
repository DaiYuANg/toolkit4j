package org.toolkit4j.collection.table;

import java.util.HashMap;
import java.util.Map;

public class HashTable<R, C, V> extends AbstractTable<R, C, V> {

  private final Map<R, Map<C, V>> backingMap = new HashMap<>();

  @Override
  protected Map<R, Map<C, V>> getBackingMap() {
    return backingMap;
  }

  @Override
  protected <V2> AbstractTable<R, C, V2> createInstance() {
    return new HashTable<>();
  }
}
