package org.toolkit4j.collection.tree;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * 可继承的通用树节点实现
 */
@Getter
@Setter
@Accessors(fluent = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class GenericTreeNode<ID> implements TreeNode<ID> {

  protected ID id;
  protected ID parentId;
  protected Collection<? extends TreeNode<ID>> children = List.of();
  protected String path = "";
  protected Integer weight = 0;

  public GenericTreeNode(ID id, ID parentId) {
    this(id, parentId, List.of(), "", 0);
  }

  @Override
  public TreeNode<ID> withChildren(Collection<? extends TreeNode<ID>> newChildren) {
    requireNonNull(newChildren, "children cannot be null");
    newChildren.forEach(child -> requireNonNull(child, "child node cannot be null"));
    this.children = new ArrayList<>(newChildren); // 可变实现直接修改
    return this;
  }

  @Override
  public TreeNode<ID> withPath(String newPath) {
    requireNonNull(newPath, "path cannot be null");
    if (newPath.isBlank()) throw new IllegalArgumentException("path cannot be blank");
    this.path = newPath; // 可变实现直接修改
    return this;
  }

  @Override
  public Integer weight() {
    return weight;
  }

  @Override
  public String path() {
    return path;
  }

  @Override
  public Collection<? extends TreeNode<ID>> children() {
    return children;
  }
}
