package org.toolkit4j.collection.tree;

import java.util.Set;

/**
 * 子节点为 LinkedHashSet 的树节点，保持插入顺序迭代。
 * TreeNode with LinkedHashSet children, preserves insertion-order iteration.
 *
 * @param <T> 节点数据类型
 */
public interface LinkedTreeNode<T> extends TreeNode<T> {

  @Override
  Set<TreeNode<T>> children();
}
