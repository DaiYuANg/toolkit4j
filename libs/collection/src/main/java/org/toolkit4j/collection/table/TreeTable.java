package org.toolkit4j.collection.table;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("unused")
public class TreeTable<R extends Comparable<R>, C extends Comparable<C>, V> extends AbstractTable<R, C, V> {

  private final Map<R, Map<C, V>> backingMap;

  public TreeTable() {
    this.backingMap = new TreeMap<>();
  }

  public TreeTable(Comparator<R> rowComparator) {
    this.backingMap = new TreeMap<>(rowComparator);
  }

  @Override
  protected Map<R, Map<C, V>> getBackingMap() {
    return backingMap;
  }

  @Override
  protected <V2> AbstractTable<R, C, V2> createInstance() {
    return new TreeTable<>();
  }
}
