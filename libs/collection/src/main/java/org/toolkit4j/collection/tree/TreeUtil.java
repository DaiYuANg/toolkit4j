package org.toolkit4j.collection.tree;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@UtilityClass
@SuppressWarnings({"unchecked", "unused"})
public class TreeUtil {

  /**
   * 深度优先遍历
   */
  public <ID, N extends TreeNode<ID>> List<N> dfs(@NonNull N root) {
    return Stream.concat(
        Stream.of(root),
        root.children().stream().flatMap(child -> dfs((N) child).stream())
      )
      .toList();
  }

  public <ID, N extends TreeNode<ID>> List<N> dfsMutable(@NonNull N root) {
    return new ArrayList<>(dfs(root));
  }

  /**
   * 广度优先遍历
   */
  public <ID, N extends TreeNode<ID>> List<N> bfs(@NonNull N root) {
    val result = new ArrayList<N>();
    val queue = new LinkedList<N>();
    queue.add(root);
    while (!queue.isEmpty()) {
      N node = queue.poll();
      result.add(node);
      node.children().stream().map(child -> (N) child).forEach(queue::add);
    }
    return result;
  }

  /**
   * 扁平化整个树
   */
  public <ID, N extends TreeNode<ID>> List<N> flatten(@NonNull Collection<N> roots) {
    return roots.stream()
      .flatMap(root -> dfs(root).stream())
      .toList();
  }

  public <ID, N extends TreeNode<ID>> List<N> flattenMutable(@NonNull Collection<N> roots) {
    return new ArrayList<>(flatten(roots));
  }

  /**
   * 扁平化并返回叶子节点
   */
  public <ID, N extends TreeNode<ID>> List<N> getLeaves(@NonNull Collection<N> roots) {
    return roots.stream()
      .flatMap(root -> dfs(root).stream())
      .filter(TreeNode::isLeaf)
      .toList();
  }

  public <ID, N extends TreeNode<ID>> List<N> getLeavesMutable(@NonNull Collection<N> roots) {
    return new ArrayList<>(getLeaves(roots));
  }

  /**
   * 根据条件查找第一个匹配节点
   */
  public <ID, N extends TreeNode<ID>> Optional<N> findFirst(@NonNull N root, @NonNull Predicate<N> predicate) {
    if (predicate.test(root)) return Optional.of(root);
    return root.children().stream()
      .map(child -> findFirst((N) child, predicate))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .findFirst();
  }

  /**
   * 获取路径上的所有节点，从根到指定节点
   */
  public <ID, N extends TreeNode<ID>> List<N> pathToNode(@NonNull N root, @NonNull Predicate<N> predicate) {
    val path = new ArrayList<N>();
    if (findPath(root, predicate, path)) return path;
    return Collections.emptyList();
  }

  public <ID, N extends TreeNode<ID>> List<N> pathToNodeMutable(@NonNull N root, @NonNull Predicate<N> predicate) {
    return new ArrayList<>(pathToNode(root, predicate));
  }

  private <ID, N extends TreeNode<ID>> boolean findPath(@NonNull N node, @NonNull Predicate<N> predicate, @NonNull List<N> path) {
    path.add(node);
    if (predicate.test(node)) return true;
    for (TreeNode<ID> child : node.children()) {
      if (findPath((N) child, predicate, path)) return true;
    }
    path.removeLast();
    return false;
  }
}
