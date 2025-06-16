package org.toolkit4j.collection.table;

import java.util.Map;
import java.util.WeakHashMap;

@SuppressWarnings("unused")
public class WeakTable<R, C, V> extends AbstractTable<R, C, V> {

  private final Map<R, Map<C, V>> backingMap = new WeakHashMap<>();

  @Override
  protected Map<R, Map<C, V>> getBackingMap() {
    return backingMap;
  }

  @Override
  protected <V2> AbstractTable<R, C, V2> createInstance() {
    return new WeakTable<>();
  }
}
