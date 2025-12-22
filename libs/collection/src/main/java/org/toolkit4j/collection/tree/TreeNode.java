package org.toolkit4j.collection.tree;

import lombok.val;

import java.util.Collection;
import java.util.Objects;

public interface TreeNode<ID> {

  ID id();

  ID parentId();

  Collection<? extends TreeNode<ID>> children();

  /**
   * 排序权重，越小越靠前
   */
  default Integer weight() {
    return 0;
  }

  /**
   * 路径缓存，如：1/11/111
   */
  default String path() {
    return "";
  }

  /**
   * 返回带新 children 的节点，如果实现不可变，则返回新对象
   */
  default TreeNode<ID> withChildren(Collection<? extends TreeNode<ID>> children) {
    throw new UnsupportedOperationException();
  }

  /**
   * 返回带新 path 的节点，如果实现不可变，则返回新对象
   */
  default TreeNode<ID> withPath(String path) {
    throw new UnsupportedOperationException();
  }

  /**
   * 是否是根节点
   */
  default boolean isRoot() {
    return Objects.isNull(parentId());
  }

  /**
   * 是否是叶子节点
   */
  default boolean isLeaf() {
    Collection<? extends TreeNode<ID>> c = children();
    return Objects.isNull(c) || c.isEmpty();
  }

  /**
   * 是否有子节点
   */
  default boolean hasChildren() {
    val c = children();
    return Objects.nonNull(c) && !c.isEmpty();
  }
}
