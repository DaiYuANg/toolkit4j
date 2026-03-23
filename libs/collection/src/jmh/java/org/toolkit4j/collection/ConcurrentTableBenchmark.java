package org.toolkit4j.collection;

import lombok.val;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.toolkit4j.collection.table.ConcurrentHashTable;
import org.toolkit4j.collection.table.HashTable;
import org.toolkit4j.collection.table.Table;

import java.util.concurrent.TimeUnit;

import static java.util.stream.IntStream.range;

/**
 * ConcurrentHashTable 并发写性能基准
 * ConcurrentHashTable parallel write benchmark
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2g", "-Xmx2g"})
@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 3)
public class ConcurrentTableBenchmark {

  @Param({"50000", "200000", "500000"})
  public int size;

  @Benchmark
  @Threads(4)
  public Table<String, String, Integer> concurrentHashTable_put_parallel() {
    val table = new ConcurrentHashTable<String, String, Integer>();
    range(0, size)
        .parallel()
        .forEach(i -> table.put(Integer.toHexString(i), Integer.toString(i), i));
    return table;
  }

  @Benchmark
  @Threads(4)
  public void concurrentHashTable_putAndGet_parallel(Blackhole bh, ConcurrentTableState state) {
    range(0, Math.min(10000, size))
        .parallel()
        .forEach(i -> {
          val r = Integer.toHexString(i % 1000);
          val c = Integer.toString(i);
          bh.consume(state.table.get(r, c));
        });
  }

  @State(Scope.Benchmark)
  public static class ConcurrentTableState {
    public ConcurrentHashTable<String, String, Integer> table;

    @Setup(Level.Trial)
    public void setup(ConcurrentTableBenchmark b) {
      table = new ConcurrentHashTable<>();
      range(0, b.size)
          .parallel()
          .forEach(i -> table.put(Integer.toHexString(i), Integer.toString(i), i));
    }
  }
}
