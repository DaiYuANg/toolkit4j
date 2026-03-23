package org.toolkit4j.collection;

import lombok.val;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.toolkit4j.collection.tree.ListTree;
import org.toolkit4j.collection.tree.Trees;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;

/**
 * 树构建性能基准
 * Tree build benchmark: flat list to tree conversion
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2g", "-Xmx2g"})
@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 3)
public class TreeBenchmark {

  @Param({"1000", "5000", "20000"})
  public int size;

  private List<BenchNode> flatList;

  @Setup
  public void setup() {
    val rng = RandomGenerator.getDefault();
    flatList = new ArrayList<>(size);
    flatList.add(new BenchNode(1L, null, 0));
    for (long i = 2; i <= size; i++) {
      val maxParent = Math.min(i - 1, 50);
      val parentId = maxParent > 0 ? rng.nextLong(1, maxParent + 1) : 1L;
      flatList.add(new BenchNode(i, parentId, rng.nextInt(10)));
    }
  }

  @Benchmark
  public ListTree<BenchNode> tree_buildList() {
    return Trees.buildList(flatList, BenchNode::id, BenchNode::parentId, null);
  }

  @Benchmark
  public ListTree<BenchNode> tree_buildList_withSort() {
    return Trees.buildList(flatList, BenchNode::id, BenchNode::parentId, Comparator.comparing(BenchNode::weight));
  }

  @Benchmark
  public void tree_traverse(Blackhole bh, TreeState state) {
    state.tree.roots().forEach(root -> traverse(bh, root));
  }

  private void traverse(Blackhole bh, org.toolkit4j.collection.tree.TreeNode<BenchNode> node) {
    bh.consume(node.data());
    node.children().forEach(c -> traverse(bh, c));
  }

  @State(Scope.Benchmark)
  public static class TreeState {
    public ListTree<BenchNode> tree;

    @Setup(Level.Trial)
    public void setup(TreeBenchmark b) {
      tree = Trees.buildList(b.flatList, BenchNode::id, BenchNode::parentId, null);
    }
  }
}
