package org.toolkit4j.collection;

import org.toolkit4j.collection.tree.FlatNode;

/**
 * 基准用扁平节点，避免依赖 test 包
 */
record BenchNode(Long id, Long parentId, int weight) implements FlatNode<Long> {}
