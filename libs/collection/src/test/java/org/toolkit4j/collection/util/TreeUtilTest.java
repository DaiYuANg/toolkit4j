package org.toolkit4j.collection.util;

import org.junit.jupiter.api.Test;
import lombok.val;
import org.toolkit4j.collection.tree.ListTree;
import org.toolkit4j.collection.tree.TreeNode;
import org.toolkit4j.collection.tree.Trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TreeUtilBigDataTest {

  @Test
  void testLargeTree() {
    int totalNodes = 100000;
    Random rand = new Random(42);
    List<Dept> allDepts = new ArrayList<>(totalNodes);

    allDepts.add(new Dept(1L, null, 0));
    for (long i = 2; i <= totalNodes; i++) {
      long parentId = 1 + rand.nextInt((int) Math.min(i - 1, 1000));
      int weight = rand.nextInt(10);
      allDepts.add(new Dept(i, parentId, weight));
    }

    ListTree<Dept> tree = Trees.build(allDepts);

    assertEquals(1, tree.roots().size());

    val dfsList = tree.stream().toList();
    assertEquals(totalNodes, dfsList.size());
    assertEquals(1L, dfsList.getFirst().data().id());

    val bfsList = tree.breadthFirst().toList();
    assertEquals(totalNodes, bfsList.size());
    assertEquals(1L, bfsList.getFirst().data().id());

    val secondLevelIds = tree.roots().getFirst().children().stream()
        .map(n -> n.data().id())
        .toList();
    assertFalse(secondLevelIds.isEmpty());
    assertTrue(secondLevelIds.contains(bfsList.get(1).data().id()));

    val flat = tree.stream().map(TreeNode::data).toList();
    assertEquals(totalNodes, flat.size());

    val leaves = tree.stream().filter(TreeNode::isLeaf).toList();
    assertFalse(leaves.isEmpty());
    leaves.forEach(n -> assertTrue(n.isLeaf()));

    Dept randomDept = flat.get(rand.nextInt(flat.size()));
    val path = tree.pathTo(d -> d.id().equals(randomDept.id()));
    assertFalse(path.isEmpty());
    assertEquals(randomDept.id(), path.getLast().data().id());
  }
}
