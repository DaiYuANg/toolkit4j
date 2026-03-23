package org.toolkit4j.collection.tree;

import java.util.Set;

record SetTreeNodeImpl<T>(T data, Set<TreeNode<T>> children) implements SetTreeNode<T> {}
