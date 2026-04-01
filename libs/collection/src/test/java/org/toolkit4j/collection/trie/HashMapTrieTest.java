package org.toolkit4j.collection.trie;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HashMapTrieTest {

  private HashMapTrie<Character, Integer> trie;

  @BeforeEach
  void setUp() {
    trie = new HashMapTrie<>();
  }

  private static List<Character> chars(String s) {
    return s.chars().mapToObj(c -> (char) c).toList();
  }

  @Test
  void testInsertAndSearch() {
    trie.insert(chars("hello"), 1);
    trie.insert(chars("hell"), 2);
    trie.insert(chars("world"), 3);

    assertEquals(1, trie.search(chars("hello")));
    assertEquals(2, trie.search(chars("hell")));
    assertEquals(3, trie.search(chars("world")));
    assertNull(trie.search(chars("he")));
    assertNull(trie.search(chars("helloworld")));
  }

  @Test
  void testStartsWith() {
    trie.insert(chars("hello"), 1);
    trie.insert(chars("hell"), 2);

    assertTrue(trie.startsWith(chars("he")));
    assertTrue(trie.startsWith(chars("hell")));
    assertTrue(trie.startsWith(chars("hello")));
    assertFalse(trie.startsWith(chars("world")));
    assertFalse(trie.startsWith(chars("hellox")));
  }

  @Test
  void testKeysWithPrefix() {
    trie.insert(chars("hello"), 1);
    trie.insert(chars("hell"), 2);
    trie.insert(chars("help"), 3);

    Set<List<Character>> keys = trie.keysWithPrefix(chars("hel"));
    assertEquals(3, keys.size());
    assertTrue(keys.contains(chars("hello")));
    assertTrue(keys.contains(chars("hell")));
    assertTrue(keys.contains(chars("help")));

    Set<List<Character>> empty = trie.keysWithPrefix(chars("xyz"));
    assertTrue(empty.isEmpty());
  }

  @Test
  void testDelete() {
    trie.insert(chars("hello"), 1);
    trie.insert(chars("hell"), 2);

    assertTrue(trie.delete(chars("hell")));
    assertNull(trie.search(chars("hell")));
    assertEquals(1, trie.search(chars("hello")));

    assertFalse(trie.delete(chars("hell")));
    assertFalse(trie.delete(chars("xyz")));
  }

  @Test
  void testDeleteLeafKey() {
    trie.insert(chars("hello"), 1);
    assertTrue(trie.delete(chars("hello")));
    assertNull(trie.search(chars("hello")));
  }

  @Test
  void testClear() {
    trie.insert(chars("hello"), 1);
    trie.insert(chars("world"), 2);

    assertFalse(trie.isEmpty());
    trie.clear();
    assertTrue(trie.isEmpty());
    assertNull(trie.search(chars("hello")));
    assertNull(trie.search(chars("world")));
  }

  @Test
  void testOverwrite() {
    trie.insert(chars("hello"), 1);
    trie.insert(chars("hello"), 99);

    assertEquals(99, trie.search(chars("hello")));
  }

  @Test
  void testEmptyPrefix() {
    trie.insert(chars("a"), 1);
    trie.insert(chars("ab"), 2);

    Set<List<Character>> all = trie.keysWithPrefix(List.of());
    assertEquals(2, all.size());
  }
}
