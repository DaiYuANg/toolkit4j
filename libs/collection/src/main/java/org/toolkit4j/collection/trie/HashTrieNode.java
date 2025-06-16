package org.toolkit4j.collection.trie;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class HashTrieNode<K, V> implements TrieNode<K, V> {
  private final Map<K, HashTrieNode<K, V>> children = new HashMap<>();
  private boolean isEnd = false;
  @Getter
  @Setter
  private V value;

  @Override
  public Map<K, HashTrieNode<K, V>> getChildren() {
    return children;
  }

  @Override
  public void setChild(K key, TrieNode<K, V> child) {
    children.put(key, (HashTrieNode<K, V>) child);
  }

  @Override
  public TrieNode<K, V> getChild(K key) {
    return children.get(key);
  }

  @Override
  public boolean isEnd() {
    return isEnd;
  }

  @Override
  public void setEnd(boolean isEnd) {
    this.isEnd = isEnd;
  }
}
