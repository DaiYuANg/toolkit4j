package org.toolkit4j.collection.tree;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.val;
import net.datafaker.Faker;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class TreeTest {

  private final Faker faker = new Faker();

  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Test
  void buildWrappedTree() {
    // 1. 准备测试数据：生成10个节点，构造简单的父子关系
    val nodes = new ArrayList<TestNode>();

    // 根节点ID为0，根节点数量1
    val root = new TestNode(0, null, "root");
    nodes.add(root);

    // 其他节点随机生成，parentId为0或其他节点的id，形成树
    IntStream.range(1, 1000).forEach(i -> {
      int parentId = i == 1 ? 0 : faker.random().nextInt(0, i - 1);
      nodes.add(new TestNode(i, parentId, faker.name().firstName()));
    });

    val tree = Tree.buildWrappedTree(
      nodes,
      TestNode::getId,
      TestNode::getParentId,
      node -> node.getParentId() == null // rootPredicate：parentId == null 是根
    );
//    val json = gson.toJson(tree);
//    System.out.println(json);
    assertEquals(1, tree.size());
    assertEquals(root, tree.getFirst().data());

    // 4. 递归校验树结构
    checkTreeStructure(tree, new HashSet<>());

    val allIds = new HashSet<Integer>();
    collectAllNodeIds(tree, allIds);
    assertEquals(nodes.size(), allIds.size());
  }

  private void checkTreeStructure(@NotNull List<TreeNodeWrapper<TestNode>> nodes, Set<Integer> visited) {
    for (TreeNodeWrapper<TestNode> node : nodes) {
      // 不能有重复访问节点
      assertTrue(visited.add(node.data().getId()));

      // 子节点的 parentId 应该是当前节点 id
      for (TreeNodeWrapper<TestNode> child : node.children()) {
        assertEquals(node.data().getId(), child.data().getParentId());
      }

      // 递归检查子节点
      checkTreeStructure(node.children(), visited);
    }
  }

  private void collectAllNodeIds(@NotNull List<TreeNodeWrapper<TestNode>> nodes, Set<Integer> allIds) {
    for (TreeNodeWrapper<TestNode> node : nodes) {
      allIds.add(node.data().getId());
      collectAllNodeIds(node.children(), allIds);
    }
  }
}