package org.toolkit4j.collection.tree;

import java.util.Set;

/**
 * 子节点为 Set 的树节点，兄弟无重复。
 * TreeNode with unique children (Set).
 *
 * @param <T> 节点数据类型
 */
public interface SetTreeNode<T> extends TreeNode<T> {

  @Override
  Set<TreeNode<T>> children();
}
