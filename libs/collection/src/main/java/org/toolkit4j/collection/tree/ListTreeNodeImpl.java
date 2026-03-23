package org.toolkit4j.collection.tree;

import java.util.List;

record ListTreeNodeImpl<T>(T data, List<TreeNode<T>> children) implements ListTreeNode<T> {}
