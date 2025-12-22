package org.toolkit4j.collection.util;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.val;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.toolkit4j.collection.tree.TreeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreeBuilderTest {

  private final Gson gson = new Gson();

  private final Faker faker = new Faker();

  @SneakyThrows
  @Test
  void testBuildTree() {
    List<Dept> list = List.of(
      new Dept(1L, null, 2),
      new Dept(2L, 1L, 1),
      new Dept(3L, 1L, 2),
      new Dept(4L, 2L, 1),
      new Dept(5L, null, 1)
    );

    List<Dept> tree = TreeBuilder.build(list);

    // 1. 根节点数量
    assertEquals(2, tree.size());

    // 2. 根节点排序：weight 小的在前
    assertEquals(5L, tree.get(0).id());
    assertEquals(1L, tree.get(1).id());

    // 3. 子节点数量和 path
    Dept root1 = tree.get(1);
    assertEquals(2, root1.children().size());
    assertEquals("1/2", root1.getChildren().get(0).getPath());
    assertEquals("1/3", root1.getChildren().get(1).getPath());

    Dept child2 = root1.getChildren().get(0);
    assertEquals(1, child2.getChildren().size());
    assertEquals("1/2/4", child2.getChildren().getFirst().getPath());

    val json = gson.toJson(tree);
    System.err.println(json);
  }

  @Test
  void testEmptyList() {
    List<Dept> tree = TreeBuilder.build(new ArrayList<>());
    assertTrue(tree.isEmpty());
  }

  @SneakyThrows
  @Test
  void testBuildTreeLargeData() {
    val NODE_COUNT = 1000; // 生成 1 万个节点
    final int MAX_CHILDREN = 5;   // 每个节点最多 5 个子节点
    final int MAX_DEPTH = 5;      // 树最大深度

    List<Dept> nodes = new ArrayList<>(NODE_COUNT);

    // 根节点先生成
    List<Dept> roots = new ArrayList<>();
    LongStream.rangeClosed(1, 10).mapToObj(i -> new Dept(i, null, faker.number().numberBetween(0, 10))).forEach(root -> {
      nodes.add(root);
      roots.add(root);
    });

    // 生成剩余节点
    for (long i = 11; i <= NODE_COUNT; i++) {
      // 随机选择一个已存在节点作为父节点
      Dept parent = nodes.get(faker.random().nextInt(nodes.size()));
      Dept node = new Dept(i, parent.id(), faker.number().numberBetween(0, 10));
      nodes.add(node);
    }

    long start = System.currentTimeMillis();
    List<Dept> tree = TreeBuilder.build(nodes);
    long duration = System.currentTimeMillis() - start;

    System.out.println("构建树耗时: " + duration + " ms");
    System.out.println("根节点数量: " + tree.size());

    // 简单校验：根节点数量与之前生成根节点数量一致
    assertEquals(10, tree.size());

    // 可以打印前 3 个根节点及其第一级子节点数量作为验证
    tree.stream().limit(3).forEach(root ->
      System.out.println("rootId=" + root.id() + ", children=" + root.children().size())
    );

    // 可选：将前几个节点序列化验证
    val json = gson.toJson(tree.subList(0, Math.min(5, tree.size())));
    System.err.println(json);
  }

}
