package org.toolkit4j.collection;

import lombok.val;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.toolkit4j.collection.trie.HashMapTrie;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Trie 实现性能基准
 * Trie benchmark: insert, search, startsWith, keysWithPrefix
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2g", "-Xmx2g"})
@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 3)
public class TrieBenchmark {

  @Param({"1000", "10000", "50000"})
  public int size;

  @SuppressWarnings("unchecked")
  private List<Character>[] keySequences;
  private HashMapTrie<Character, Integer> preloadedTrie;

  @Setup
  public void setup() {
    keySequences = IntStream.range(0, size)
        .<List<Character>>mapToObj(i -> ("key_" + i + "_suffix").chars().mapToObj(c -> (char) c).toList())
        .toArray(List[]::new);
    preloadedTrie = new HashMapTrie<>();
    for (int i = 0; i < size; i++) {
      preloadedTrie.insert(keySequences[i], i);
    }
  }

  @Benchmark
  public HashMapTrie<Character, Integer> trie_insert() {
    val trie = new HashMapTrie<Character, Integer>();
    for (int i = 0; i < size; i++) {
      trie.insert(keySequences[i], i);
    }
    return trie;
  }

  @Benchmark
  public void trie_search(Blackhole bh) {
    for (int i = 0; i < size; i++) {
      bh.consume(preloadedTrie.search(keySequences[i]));
    }
  }

  @Benchmark
  public void trie_startsWith(Blackhole bh) {
    val prefix = List.of('k', 'e', 'y', '_');
    for (int i = 0; i < 100; i++) {
      bh.consume(preloadedTrie.startsWith(prefix));
    }
  }

  @Benchmark
  public void trie_keysWithPrefix(Blackhole bh) {
    val prefix = List.of('k', 'e', 'y', '_', '1');
    bh.consume(preloadedTrie.keysWithPrefix(prefix));
  }
}
