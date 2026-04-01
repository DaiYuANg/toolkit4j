package org.toolkit4j.collection.tree;

import java.util.Set;

/**
 * roots 为 Set 的树，根节点无重复。 Tree with unique roots (Set).
 *
 * @param <T> 节点数据类型
 */
public interface SetTree<T> extends Tree<T> {

  @Override
  Set<TreeNode<T>> roots();
}
