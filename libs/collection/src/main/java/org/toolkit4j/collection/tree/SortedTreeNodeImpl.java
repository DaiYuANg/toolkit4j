package org.toolkit4j.collection.tree;

import java.util.SortedSet;

record SortedTreeNodeImpl<T>(T data, SortedSet<TreeNode<T>> children)
    implements SortedTreeNode<T> {}
