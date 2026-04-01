package org.toolkit4j.collection.tree;

import java.util.SortedSet;

/**
 * roots 为 SortedSet 的树，根与子节点按 comparator 排序。 Tree with SortedSet roots, ordered by comparator.
 *
 * @param <T> 节点数据类型
 */
public interface SortedTree<T> extends Tree<T> {

  @Override
  SortedSet<TreeNode<T>> roots();
}
