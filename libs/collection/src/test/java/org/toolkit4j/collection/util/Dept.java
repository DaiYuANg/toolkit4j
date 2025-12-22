package org.toolkit4j.collection.util;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.toolkit4j.collection.tree.GenericTreeNode;
import org.toolkit4j.collection.tree.TreeNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
class Dept extends GenericTreeNode<Long> {
  private final Long id;
  private final Long parentId;
  private List<Dept> children = new ArrayList<>();
  private String path = "";
  private final int weight;

  Dept(Long id, Long parentId, int weight) {
    this.id = id;
    this.parentId = parentId;
    this.weight = weight;
  }

  public Dept withChildren(@NonNull Collection<? extends TreeNode<Long>> children) {
    this.children = new ArrayList<>();
    children.forEach(child -> this.children.add((Dept) child));
    return this;
  }

  public Dept withPath(String path) {
    this.path = path;
    return this;
  }

  public boolean isLeaf() {
    return children.isEmpty();
  }

  public boolean isRoot() {
    return parentId == null || parentId == 0;
  }

  public boolean hasChildren() {
    return !children.isEmpty();
  }
}
