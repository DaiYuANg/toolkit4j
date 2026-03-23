package org.toolkit4j.collection.tree;

import java.util.Set;
import java.util.stream.Stream;

/**
 * roots 为 LinkedHashSet 的树，保持插入顺序迭代。
 * Tree with roots as LinkedHashSet, preserves insertion-order iteration.
 *
 * @param <T> 节点数据类型
 */
public interface LinkedTree<T> extends Tree<T> {

  @Override
  Set<TreeNode<T>> roots();
}
