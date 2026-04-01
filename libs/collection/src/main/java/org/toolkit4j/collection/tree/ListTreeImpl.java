package org.toolkit4j.collection.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import lombok.val;
import org.jetbrains.annotations.NotNull;

record ListTreeImpl<T>(List<TreeNode<T>> roots) implements ListTree<T> {

  @Override
  public Stream<TreeNode<T>> stream() {
    return roots.stream().flatMap(TreeNode::stream);
  }

  @Override
  public @NotNull Stream<TreeNode<T>> breadthFirst() {
    val result = new ArrayList<TreeNode<T>>();
    val queue = new LinkedList<>(roots);
    while (!queue.isEmpty()) {
      val node = queue.poll();
      result.add(node);
      queue.addAll(node.children());
    }
    return result.stream();
  }
}
