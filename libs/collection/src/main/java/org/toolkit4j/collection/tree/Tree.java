package org.toolkit4j.collection.tree;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@UtilityClass
public class Tree {
  public <T, ID> List<TreeNodeWrapper<T>> buildWrappedTree(
    @NotNull List<T> nodes,
    Function<T, ID> idGetter,
    Function<T, ID> parentIdGetter,
    Predicate<T> rootPredicate
  ) {
    // 1. 将原始节点包装成 TreeNodeWrapper<T>
    val wrapperMap = nodes.stream()
      .collect(Collectors.toMap(
        idGetter,
        node -> new TreeNodeWrapper<>(node, new ArrayList<>())
      ));

    // 2. 构造 Tree<TreeNodeWrapper<T>, ID> 来复用构建逻辑
    val tree = NaryTreeBuilder.<TreeNodeWrapper<T>, ID>builder()
      .nodes(new ArrayList<>(wrapperMap.values()))
      .idGetter(wrapper -> idGetter.apply(wrapper.data()))
      .parentIdGetter(wrapper -> parentIdGetter.apply(wrapper.data()))
      .childrenGetter(TreeNodeWrapper::children)
      .childrenSetter((parent, children) -> parent.children().addAll(children))
      .newListSupplier(ArrayList::new)
      .rootPredicate(wrapper -> rootPredicate.test(wrapper.data()))
      .build();

    // 3. 调用 Tree 构建树
    return tree.buildTree();
  }
}
