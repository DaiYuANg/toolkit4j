package org.toolkit4j.collection.tree;

import lombok.val;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;

record ListTreeImpl<T>(List<TreeNode<T>> roots) implements ListTree<T> {

  @Override
  public Stream<TreeNode<T>> stream() {
    return roots.stream().flatMap(TreeNode::stream);
  }

  @Override
  public Stream<TreeNode<T>> breadthFirst() {
    val result = new ArrayList<TreeNode<T>>();
    val queue = new LinkedList<TreeNode<T>>(roots);
    while (!queue.isEmpty()) {
      var node = queue.poll();
      result.add(node);
      queue.addAll(node.children());
    }
    return result.stream();
  }
}
