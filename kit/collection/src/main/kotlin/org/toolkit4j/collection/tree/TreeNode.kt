// (C)2023
package org.toolkit4j.collection.tree

import java.util.Objects
import java.util.function.Consumer
import java.util.stream.Stream

interface TreeNode<T, out C : MutableCollection<T>> {
    var data: T

    var parent: TreeNode<T, @UnsafeVariance C>?

    val children: MutableCollection<TreeNode<T, @UnsafeVariance C>?>

    fun isRoot(): Boolean {
        return Objects.isNull(parent)
    }

    fun isLeaf(): Boolean {
        return children.isEmpty()
    }

    fun addChild(child: TreeNode<T, @UnsafeVariance C>?)

    fun addChildren(children: Collection<TreeNode<T, @UnsafeVariance C>?>)

    fun removeChild(child: TreeNode<T, @UnsafeVariance C>)

    fun stream(): Stream<TreeNode<T, @UnsafeVariance C>?>

    fun parallelStream(): Stream<TreeNode<T, @UnsafeVariance C>?>

    fun walk(action: Consumer<TreeNode<T, @UnsafeVariance C>?>)

    fun parallelWalk(action: Consumer<TreeNode<T, @UnsafeVariance C>?>)

    fun iterator(): Iterator<TreeNode<T, C>?>
}
