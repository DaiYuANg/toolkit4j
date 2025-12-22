package org.toolkit4j.collection.tree;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

@UtilityClass
public class TreeBuilder {

  private final String separator = "/";

  /**
   * 构建树形结构
   */
  public <ID, N extends TreeNode<ID>> @NonNull List<N> build(
    @NonNull Collection<N> nodes,
    @NonNull Function<N, Integer> weightGetter
  ) {
    // 1. 构建 id -> node 映射
    val nodeMap = nodes.stream()
      .collect(Collectors.toMap(TreeNode::id, n -> n));

    // 2. 构建 parentId -> children 映射
    val childrenMap = new HashMap<ID, List<N>>();
    nodes.forEach(node -> {
      val parentId = node.parentId();
      if (parentId != null && nodeMap.containsKey(parentId)) {
        childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(node);
      }
    });

    // 3. 构建根节点树
    val roots = nodes.stream()
      .filter(TreeNode::isRoot)
      .map(root -> buildNodeRecursively(root, childrenMap, weightGetter, null))
      .sorted(comparingInt(weightGetter::apply))
      .toList();

    return List.copyOf(roots);
  }

  /**
   * 默认构建，使用节点 weight
   */
  public <ID, N extends TreeNode<ID>> @NonNull List<N> build(@NonNull Collection<N> nodes) {
    return build(nodes, TreeNode::weight);
  }

  /**
   * 递归构建节点，生成 children + path + 排序
   */
  @SuppressWarnings("unchecked")
  private <ID, N extends TreeNode<ID>> N buildNodeRecursively(
    @NonNull N node,
    @NonNull Map<ID, List<N>> childrenMap,
    @NonNull Function<N, Integer> weightGetter,
    String parentPath
  ) {
    val path = parentPath == null ? String.valueOf(node.id()) : "%s%s%s".formatted(parentPath, separator, node.id());

    val children = childrenMap.getOrDefault(node.id(), Collections.emptyList()).stream()
      .map(child -> buildNodeRecursively(child, childrenMap, weightGetter, path))
      .sorted(comparingInt(weightGetter::apply))
      .toList();

    return (N) node.withChildren(children).withPath(path);
  }
}
