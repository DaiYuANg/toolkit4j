package org.toolkit4j.collection;

import static java.util.stream.IntStream.range;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import lombok.val;
import org.jspecify.annotations.NonNull;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.toolkit4j.collection.table.ConcurrentHashTable;
import org.toolkit4j.collection.table.HashTable;
import org.toolkit4j.collection.table.Table;

/** 表实现性能基准：HashTable vs ConcurrentHashTable Table benchmark: HashTable vs ConcurrentHashTable */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(
    value = 1,
    jvmArgs = {"-Xms2g", "-Xmx2g"})
@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 3)
public class TableBenchmark {

  @Param({"1000", "10000", "100000"})
  public int size;

  private int[] keys;

  @Setup
  public void setup() {
    keys = range(0, size).toArray();
  }

  // --- HashTable 单线程 ---
  @Benchmark
  public Table<String, String, Integer> hashTable_put_sequential() {
    val table = new HashTable<String, String, Integer>();
    for (int i = 0; i < size; i++) {
      val r = "r" + (keys[i] % 100);
      val c = "c" + keys[i];
      table.put(r, c, i);
    }
    return table;
  }

  @Benchmark
  public void hashTable_get_sequential(Blackhole bh, HashTableState state) {
    for (int i = 0; i < size; i++) {
      val r = "r" + (keys[i] % 100);
      val c = "c" + keys[i];
      bh.consume(state.table.get(r, c));
    }
  }

  @Benchmark
  public void hashTable_putAndGet_sequential(Blackhole bh) {
    val table = new HashTable<String, String, Integer>();
    for (int i = 0; i < size; i++) {
      val r = "r" + (keys[i] % 100);
      val c = "c" + keys[i];
      table.put(r, c, i);
    }
    for (int i = 0; i < size; i++) {
      val r = "r" + (keys[i] % 100);
      val c = "c" + keys[i];
      bh.consume(table.get(r, c));
    }
  }

  // --- ConcurrentHashTable 单线程 ---
  @Benchmark
  public Table<String, String, Integer> concurrentHashTable_put_sequential() {
    val table = new ConcurrentHashTable<String, String, Integer>();
    for (int i = 0; i < size; i++) {
      val r = "r" + (keys[i] % 100);
      val c = "c" + keys[i];
      table.put(r, c, i);
    }
    return table;
  }

  @Benchmark
  public void concurrentHashTable_get_sequential(Blackhole bh, ConcurrentHashTableState state) {
    for (int i = 0; i < size; i++) {
      val r = "r" + (keys[i] % 100);
      val c = "c" + keys[i];
      bh.consume(state.table.get(r, c));
    }
  }

  // --- ConcurrentHashTable 多线程写 ---
  @Benchmark
  @Threads(4)
  public Table<String, String, Integer> concurrentHashTable_put_parallel() {
    val table = new ConcurrentHashTable<String, String, Integer>();
    range(0, size).parallel().forEach(i -> table.put("r" + (i % 100), "c" + i, i));
    return table;
  }

  @Benchmark
  @Threads(4)
  public void concurrentHashTable_get_parallel(Blackhole bh, ConcurrentHashTableState state) {
    range(0, size).parallel().forEach(i -> bh.consume(state.table.get("r" + (i % 100), "c" + i)));
  }

  @State(Scope.Benchmark)
  public static class HashTableState {
    public HashTable<String, String, Integer> table;

    @Setup(Level.Trial)
    public void setup(@NonNull TableBenchmark b) {
      table = new HashTable<>();
      for (int i = 0; i < b.size; i++) {
        val r = "r" + (i % 100);
        val c = "c" + i;
        table.put(r, c, i);
      }
    }
  }

  @State(Scope.Benchmark)
  public static class ConcurrentHashTableState {
    public ConcurrentHashTable<String, String, Integer> table;

    @Setup(Level.Trial)
    public void setup(TableBenchmark b) {
      table = new ConcurrentHashTable<>();
      IntStream.range(0, b.size).forEach(i -> table.put("r" + (i % 100), "c" + i, i));
    }
  }
}
