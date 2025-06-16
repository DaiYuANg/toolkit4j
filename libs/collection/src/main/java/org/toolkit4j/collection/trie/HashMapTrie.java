package org.toolkit4j.collection.trie;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

@ApiStatus.Experimental
public class HashMapTrie<K, V> implements Trie<K, V> {
  private final TrieNode<K, V> root;
  private final Supplier<TrieNode<K, V>> nodeSupplier;

  public HashMapTrie() {
    this(HashTrieNode::new);
  }

  public HashMapTrie(@NotNull Supplier<TrieNode<K, V>> nodeSupplier) {
    this.nodeSupplier = nodeSupplier;
    this.root = nodeSupplier.get();
  }

  @Override
  public void insert(Iterable<K> keySequence, V value) {
    var node = root;
    for (K key : keySequence) {
      var child = node.getChild(key);
      if (child == null) {
        child = nodeSupplier.get();
        node.setChild(key, child);
      }
      node = child;
    }
    node.setEnd(true);
    node.setValue(value);
  }

  @Override
  public V search(Iterable<K> keySequence) {
    return traverse(keySequence).filter(TrieNode::isEnd)
      .map(TrieNode::getValue)
      .orElse(null);
  }

  @Override
  public boolean startsWith(Iterable<K> prefixSequence) {
    return traverse(prefixSequence).isPresent();
  }


  @Override
  public boolean delete(Iterable<K> keySequence) {
    return delete(root, keySequence.iterator());
  }

  private boolean delete(TrieNode<K, V> current, Iterator<K> iterator) {
    if (!iterator.hasNext()) {
      if (!current.isEnd()) return false;
      current.setEnd(false);
      current.setValue(null);
      return current.getChildren().isEmpty();
    }
    K key = iterator.next();
    var child = current.getChild(key);
    if (child == null) return false;

    boolean shouldDeleteChild = delete(child, iterator);

    if (shouldDeleteChild) {
      current.getChildren().remove(key);
      return !current.isEnd() && current.getChildren().isEmpty();
    }
    return false;
  }


  @Override
  public Set<List<K>> keysWithPrefix(Iterable<K> prefixSequence) {
    return traverse(prefixSequence)
      .map(node -> {
        LinkedList<K> prefixList = new LinkedList<>();
        prefixSequence.forEach(prefixList::add);
        Set<List<K>> results = new HashSet<>();
        collect(node, prefixList, results);
        return results;
      })
      .orElseGet(Collections::emptySet);
  }

  private void collect(TrieNode<K, V> node, LinkedList<K> path, Set<List<K>> results) {
    if (node.isEnd()) {
      results.add(new ArrayList<>(path));
    }
    node.getChildren().forEach((key, value) -> {
      path.addLast(key);
      collect(value, path, results);
      path.removeLast();
    });
  }

  @Override
  public void clear() {
    root.getChildren().clear();
    root.setEnd(false);
    root.setValue(null);
  }

  @Override
  public boolean isEmpty() {
    return root.getChildren().isEmpty();
  }

  private Optional<TrieNode<K, V>> traverse(@NotNull Iterable<K> sequence) {
    TrieNode<K, V> node = root;
    for (K key : sequence) {
      node = node.getChild(key);
      if (node == null) return Optional.empty();
    }
    return Optional.of(node);
  }

}
