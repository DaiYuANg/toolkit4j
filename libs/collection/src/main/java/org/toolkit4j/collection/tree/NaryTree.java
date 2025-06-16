package org.toolkit4j.collection.tree;

import io.soabase.recordbuilder.core.RecordBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.val;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@RecordBuilder
@ApiStatus.Experimental
public record NaryTree<T, ID>(
  List<T> nodes,
  Function<T, ID> idGetter,
  Function<T, ID> parentIdGetter,
  Function<T, List<T>> childrenGetter,
  BiConsumer<T, List<T>> childrenSetter, // 设置 children 的方式
  Supplier<List<T>> newListSupplier,
  Predicate<T> rootPredicate,
  Integer maxDepth
) {

  public List<T> buildTree() {
    val nodeMap = new Object2ObjectOpenHashMap<>(
      nodes.stream()
        .collect(
          toMap(idGetter, identity())
        )
    );
    val depthMap = new Object2IntOpenHashMap<ID>();
    val roots = newListSupplier.get();

    for (T node : nodes) {
      if (rootPredicate.test(node)) {
        roots.add(node);
        depthMap.put(idGetter.apply(node), 0);
        continue;
      }

      ID parentId = parentIdGetter.apply(node);
      val parentDepth = depthMap.getOrDefault(parentId, MIN_VALUE);
      if (parentDepth == MIN_VALUE) {
        // 父节点不存在，跳过
        continue;
      }

      val currentDepth = parentDepth + 1;
      if (ofNullable(maxDepth).filter(d -> d >= 0).map(d -> currentDepth >= d).orElse(false)) {
        continue;
      }

      val parentOpt = ofNullable(nodeMap.get(parentId));
      if (parentOpt.isEmpty()) {
        continue;
      }

      val parent = parentOpt.get();

      // 使用 Optional 来获取和设置 children
      val children = ofNullable(childrenGetter.apply(parent))
        .orElseGet(() -> {
          val newChildren = newListSupplier.get();
          childrenSetter.accept(parent, newChildren);
          return newChildren;
        });

      children.add(node);
      depthMap.put(idGetter.apply(node), currentDepth);
    }

    return roots;
  }

}
