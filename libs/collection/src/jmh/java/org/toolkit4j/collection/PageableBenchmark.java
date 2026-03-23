package org.toolkit4j.collection;

import lombok.val;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.toolkit4j.collection.pageable.PageableList;
import org.toolkit4j.collection.pageable.PageableSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 分页集合性能基准
 * Pageable collection benchmark
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2g", "-Xmx2g"})
@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 3)
public class PageableBenchmark {

  @Param({"10000", "100000", "500000"})
  public int size;

  private PageableList<Integer> pageableList;
  private PageableSet<Integer> pageableSet;

  @Setup
  public void setup() {
    val list = IntStream.range(0, size).boxed().toList();
    pageableList = new PageableList<>(new ArrayList<>(list));
    pageableSet = new PageableSet<>(new HashSet<>(list));
  }

  @Benchmark
  public void pageableList_page(Blackhole bh) {
    val pageSize = 100;
    for (int p = 1; p <= size / pageSize; p++) {
      bh.consume(pageableList.page(p, pageSize));
    }
  }

  @Benchmark
  public void pageableList_slice(Blackhole bh) {
    bh.consume(pageableList.slice(0, 1000));
    bh.consume(pageableList.slice(size / 2, size / 2 + 1000));
  }

  @Benchmark
  public void pageableSet_page(Blackhole bh) {
    val pageSize = 100;
    for (int p = 1; p <= Math.min(100, size / pageSize); p++) {
      bh.consume(pageableSet.page(p, pageSize));
    }
  }

  @Benchmark
  public void pageableList_stream(Blackhole bh) {
    val count = pageableList.stream().count();
    bh.consume(count);
  }
}
