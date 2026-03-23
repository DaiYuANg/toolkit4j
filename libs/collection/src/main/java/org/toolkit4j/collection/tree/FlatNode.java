package org.toolkit4j.collection.tree;

/**
 * 扁平节点的最小契约，用于从扁平列表构建树。
 * Minimal contract for flat nodes when building a tree from a flat list.
 *
 * @param <ID> 节点 ID 类型
 */
public interface FlatNode<ID> {

  ID id();

  ID parentId();
}
