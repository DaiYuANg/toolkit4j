package org.toolkit4j.collection.table;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;

public class HashTable<R, C, V> extends AbstractTable<R, C, V> {

  private final Map<R, Map<C, V>> backingMap = new Object2ObjectOpenHashMap<>();

  @Override
  protected Map<R, Map<C, V>> getBackingMap() {
    return backingMap;
  }

  @Override
  protected Map<C, V> createColumnMap() {
    return new Object2ObjectOpenHashMap<>();
  }

  @Override
  protected <V2> AbstractTable<R, C, V2> createInstance() {
    return new HashTable<>();
  }
}
