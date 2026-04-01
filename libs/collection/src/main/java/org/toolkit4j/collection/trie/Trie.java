package org.toolkit4j.collection.trie;

import java.util.List;
import java.util.Set;

public interface Trie<K, V> {

  /**
   * 插入一个键序列及其对应的值，如果键已存在则覆盖
   *
   * @param keySequence 键序列，如字符序列或泛型序列
   * @param value 关联的值
   */
  void insert(Iterable<K> keySequence, V value);

  /**
   * 查找完整键对应的值
   *
   * @param keySequence 键序列
   * @return 值，如果不存在则返回 null
   */
  V search(Iterable<K> keySequence);

  /**
   * 判断是否存在以指定前缀开头的键
   *
   * @param prefixSequence 前缀键序列
   * @return 是否存在该前缀
   */
  boolean startsWith(Iterable<K> prefixSequence);

  /**
   * 删除完整键及对应的值
   *
   * @param keySequence 键序列
   * @return 是否成功删除（键存在且被删除）
   */
  boolean delete(Iterable<K> keySequence);

  /**
   * 返回所有以指定前缀开头的键集合（完整键的列表）
   *
   * @param prefixSequence 前缀键序列
   * @return 所有匹配的完整键集合
   */
  Set<List<K>> keysWithPrefix(Iterable<K> prefixSequence);

  /** 清空整个 Trie */
  void clear();

  /**
   * Trie 是否为空
   *
   * @return true 空，false 非空
   */
  boolean isEmpty();
}
