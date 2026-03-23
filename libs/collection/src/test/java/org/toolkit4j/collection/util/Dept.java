package org.toolkit4j.collection.util;

import org.toolkit4j.collection.tree.FlatNode;

record Dept(Long id, Long parentId, int weight) implements FlatNode<Long> {}
