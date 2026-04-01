package org.toolkit4j.collection.trie;

import java.util.Map;

public interface TrieNode<K, V> {
  Map<K, ? extends TrieNode<K, V>> getChildren();

  void setChild(K key, TrieNode<K, V> child);

  TrieNode<K, V> getChild(K key);

  boolean isEnd();

  void setEnd(boolean isEnd);

  V getValue();

  void setValue(V value);
}
