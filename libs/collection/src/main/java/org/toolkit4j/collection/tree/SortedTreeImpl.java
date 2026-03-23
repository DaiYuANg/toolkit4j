package org.toolkit4j.collection.tree;

import lombok.val;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

record SortedTreeImpl<T>(SortedSet<TreeNode<T>> roots) implements SortedTree<T> {

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
