package org.toolkit4j.collection.tree;

import java.util.SortedSet;

/**
 * 子节点为 SortedSet 的树节点，兄弟按 comparator 排序。 TreeNode with sorted children (SortedSet).
 *
 * @param <T> 节点数据类型
 */
public interface SortedTreeNode<T> extends TreeNode<T> {

  @Override
  SortedSet<TreeNode<T>> children();
}
