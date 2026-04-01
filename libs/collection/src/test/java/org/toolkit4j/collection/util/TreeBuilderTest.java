package org.toolkit4j.collection.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.LongStream;
import lombok.SneakyThrows;
import lombok.val;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.toolkit4j.collection.tree.ListTree;
import org.toolkit4j.collection.tree.TreeNode;
import org.toolkit4j.collection.tree.Trees;

class TreeBuilderTest {

  private final Gson gson = new Gson();
  private final Faker faker = new Faker();

  @SneakyThrows
  @Test
  void testBuildTree() {
    List<Dept> list =
        List.of(
            new Dept(1L, null, 2),
            new Dept(2L, 1L, 1),
            new Dept(3L, 1L, 2),
            new Dept(4L, 2L, 1),
            new Dept(5L, null, 1));

    ListTree<Dept> tree = Trees.build(list, Comparator.comparing(Dept::weight));

    assertEquals(2, tree.roots().size());
    assertEquals(5L, tree.roots().get(0).data().id());
    assertEquals(1L, tree.roots().get(1).data().id());

    val root1 = tree.roots().get(1);
    val root1Children = root1.children().stream().toList();
    assertEquals(2, root1Children.size());
    assertEquals(2L, root1Children.get(0).data().id());
    assertEquals(3L, root1Children.get(1).data().id());

    val child2 = root1Children.get(0);
    val child2Children = child2.children().stream().toList();
    assertEquals(1, child2Children.size());
    assertEquals(4L, child2Children.getFirst().data().id());

    val rootsForJson = tree.roots().stream().map(this::toSimple).toList();
    System.err.println(gson.toJson(rootsForJson));
  }

  private Object toSimple(TreeNode<Dept> n) {
    return new Object() {
      public final long id = n.data().id();
      public final Object children =
          n.children().stream().map(TreeBuilderTest.this::toSimple).toList();
    };
  }

  @Test
  void testEmptyList() {
    ListTree<Dept> tree = Trees.build(new ArrayList<>());
    assertTrue(tree.roots().isEmpty());
  }

  @Test
  void testBuildSet() {
    List<Dept> list = List.of(new Dept(1L, null, 2), new Dept(2L, 1L, 1));
    val setTree = Trees.buildSet(list);
    assertEquals(1, setTree.roots().size());
    assertTrue(setTree.roots().stream().anyMatch(n -> n.data().id() == 1L));
  }

  @Test
  void testBuildLinked() {
    List<Dept> list = List.of(new Dept(1L, null, 2), new Dept(2L, 1L, 1), new Dept(3L, 1L, 2));
    val linkedTree = Trees.buildLinked(list);
    assertEquals(1, linkedTree.roots().size());
    val root = linkedTree.roots().iterator().next();
    assertEquals(2, root.children().size());
    val childIds = root.children().stream().map(n -> n.data().id()).toList();
    assertEquals(List.of(2L, 3L), childIds);
  }

  @Test
  void testBuildSorted() {
    List<Dept> list =
        List.of(
            new Dept(1L, null, 2), new Dept(2L, 1L, 1), new Dept(3L, 1L, 0), new Dept(5L, null, 1));
    val sortedTree = Trees.buildSorted(list, Comparator.comparing(Dept::weight));
    val rootIds = sortedTree.roots().stream().map(n -> n.data().id()).toList();
    assertEquals(List.of(5L, 1L), rootIds);
    val root1 =
        sortedTree.roots().stream().filter(n -> n.data().id() == 1L).findFirst().orElseThrow();
    val childIds = root1.children().stream().map(n -> n.data().id()).toList();
    assertEquals(List.of(3L, 2L), childIds);
  }

  @SneakyThrows
  @Test
  void testBuildTreeLargeData() {
    val NODE_COUNT = 1000;
    final int MAX_CHILDREN = 5;
    final int MAX_DEPTH = 5;

    List<Dept> nodes = new ArrayList<>(NODE_COUNT);
    List<Dept> roots = new ArrayList<>();
    LongStream.rangeClosed(1, 10)
        .mapToObj(i -> new Dept(i, null, faker.number().numberBetween(0, 10)))
        .forEach(
            node -> {
              nodes.add(node);
              roots.add(node);
            });

    for (long i = 11; i <= NODE_COUNT; i++) {
      Dept parent = nodes.get(faker.random().nextInt(nodes.size()));
      nodes.add(new Dept(i, parent.id(), faker.number().numberBetween(0, 10)));
    }

    long start = System.currentTimeMillis();
    ListTree<Dept> tree = Trees.build(nodes, Comparator.comparing(Dept::weight));
    long duration = System.currentTimeMillis() - start;

    System.out.println("构建树耗时: " + duration + " ms");
    System.out.println("根节点数量: " + tree.roots().size());
    assertEquals(10, tree.roots().size());

    tree.roots().stream()
        .limit(3)
        .forEach(
            root ->
                System.out.println(
                    "rootId=" + root.data().id() + ", children=" + root.children().size()));

    val rootsForJson = tree.roots().stream().limit(5).map(this::toSimple).toList();
    System.err.println(gson.toJson(rootsForJson));
  }
}
