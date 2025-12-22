package org.toolkit4j.collection.util;

import org.junit.jupiter.api.Test;
import org.toolkit4j.collection.tree.TreeBuilder;
import org.toolkit4j.collection.tree.TreeNode;
import org.toolkit4j.collection.tree.TreeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TreeUtilBigDataTest {

  @Test
  void testLargeTree() {
    // 使用 Faker 生成大数据量
    int totalNodes = 100000;
    Random rand = new Random(42);
    List<Dept> allDepts = new ArrayList<>(totalNodes);

    // 根节点
    allDepts.add(new Dept(1L, null, 0));

    // 随机生成子节点
    for (long i = 2; i <= totalNodes; i++) {
      // 随机选择已有节点作为 parentId，保证树结构
      long parentId = 1 + rand.nextInt((int) Math.min(i - 1, 1000));
      int weight = rand.nextInt(10);
      allDepts.add(new Dept(i, parentId, weight));
    }

    // 构建树
    List<Dept> roots = TreeBuilder.build(allDepts);

    // 测试根节点数量
    assertEquals(1, roots.size());
    assertTrue(roots.getFirst().isRoot());

    // 测试 DFS
    List<Dept> dfsList = TreeUtil.dfs(roots.getFirst());
    assertEquals(totalNodes, dfsList.size());

    // DFS 中首个节点应该是根节点
    assertEquals(1L, dfsList.getFirst().id());

    // 测试 BFS
    List<Dept> bfsList = TreeUtil.bfs(roots.getFirst());
    assertEquals(totalNodes, bfsList.size());
    assertEquals(1L, bfsList.getFirst().id());

    // 检查 BFS 的第二层节点 id 是否在合理范围
    List<Long> secondLevelIds = roots.getFirst().children().stream()
      .map(TreeNode::id)
      .toList();
    assertFalse(secondLevelIds.isEmpty());
    assertTrue(secondLevelIds.contains(bfsList.get(1).id()));

    // 扁平化整个树
    List<Dept> flat = TreeUtil.flatten(roots);
    assertEquals(totalNodes, flat.size());

    // 获取叶子节点
    List<Dept> leaves = TreeUtil.getLeaves(roots);
    assertFalse(leaves.isEmpty());
    leaves.forEach(dept -> assertTrue(dept.isLeaf()));

    // 测试 pathToNode
    Dept randomNode = flat.get(rand.nextInt(flat.size()));
    List<Dept> path = TreeUtil.pathToNode(roots.getFirst(), n -> n.id().equals(randomNode.id()));
    assertFalse(path.isEmpty());
    assertEquals(randomNode.id(), path.getLast().id());
  }
}
