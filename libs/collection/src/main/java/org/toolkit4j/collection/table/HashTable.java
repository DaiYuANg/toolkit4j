package org.toolkit4j.collection.table;

import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class HashTable implements Table{
  @Override
  public Object get(Object rowKey, Object columnKey) {
    return null;
  }

  @Override
  public void put(Object rowKey, Object columnKey, Object value) {

  }

  @Override
  public Object remove(Object rowKey, Object columnKey) {
    return null;
  }

  @Override
  public boolean contains(Object rowKey, Object columnKey) {
    return false;
  }

  @Override
  public boolean containsRow(Object rowKey) {
    return false;
  }

  @Override
  public boolean containsColumn(Object columnKey) {
    return false;
  }

  @Override
  public Map row(Object rowKey) {
    return Map.of();
  }

  @Override
  public Map column(Object columnKey) {
    return Map.of();
  }

  @Override
  public Map rowMap() {
    return Map.of();
  }

  @Override
  public Set<Cell> cellSet() {
    return Set.of();
  }

  @Override
  public void clear() {

  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public Table filter(BiPredicate predicate) {
    return null;
  }

  @Override
  public Table mapValues(Function mapper) {
    return null;
  }
}
