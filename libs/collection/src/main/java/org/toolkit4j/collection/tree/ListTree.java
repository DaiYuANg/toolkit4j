package org.toolkit4j.collection.tree;

import java.util.List;

/**
 * roots 为 List 的树，保持根节点顺序。 Tree with ordered roots (List).
 *
 * @param <T> 节点数据类型
 */
public interface ListTree<T> extends Tree<T> {

  @Override
  List<TreeNode<T>> roots();
}
