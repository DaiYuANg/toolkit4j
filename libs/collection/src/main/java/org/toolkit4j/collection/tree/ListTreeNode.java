package org.toolkit4j.collection.tree;

import java.util.List;

/**
 * 子节点为 List 的树节点，保持兄弟顺序。
 * TreeNode with ordered children (List).
 *
 * @param <T> 节点数据类型
 */
public interface ListTreeNode<T> extends TreeNode<T> {

  @Override
  List<TreeNode<T>> children();
}
