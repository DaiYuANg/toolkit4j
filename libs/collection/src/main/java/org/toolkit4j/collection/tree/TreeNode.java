package org.toolkit4j.collection.tree;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * 树节点接口。TreeNode interface.
 *
 * <p>子类可细化 children 的集合类型，如 ListTreeNode、SetTreeNode。 Sub-interfaces may narrow children to List or
 * Set.
 *
 * @param <T> 节点数据类型
 */
public interface TreeNode<T> {

  /** 节点数据。Node data. */
  T data();

  /** 子节点集合。Children collection. 实现可为 List 或 Set 等。 */
  Collection<TreeNode<T>> children();

  /** 是否叶子节点。True if no children. */
  default boolean isLeaf() {
    return children().isEmpty();
  }

  /** 以该节点为根的 DFS 流。DFS stream of subtree. */
  default Stream<TreeNode<T>> stream() {
    return Stream.concat(Stream.of(this), children().stream().flatMap(TreeNode::stream));
  }
}
