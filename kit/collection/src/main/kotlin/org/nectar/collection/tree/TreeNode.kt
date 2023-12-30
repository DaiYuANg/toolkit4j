// (C)2023
package org.nectar.collection.tree

import java.util.Objects
import java.util.function.Consumer
import java.util.stream.Stream

interface TreeNode<T> {
    var data: T?

    var parent: TreeNode<T>?

    val children: MutableCollection<TreeNode<T>?>

    fun isRoot(): Boolean {
        return Objects.isNull(parent)
    }

    fun isLeaf(): Boolean {
        return children.isEmpty()
    }

    fun addChild(child: TreeNode<T>?)

    fun addChildren(children: Collection<TreeNode<T>?>?)

    fun removeChild(child: TreeNode<T>?)

    fun stream(): Stream<TreeNode<T>?>?

    fun parallelStream(): Stream<TreeNode<T>?>?

    fun walk(action: Consumer<TreeNode<T>?>)

    fun parallelWalk(action: Consumer<TreeNode<T>?>)

    fun iterator(): Iterator<TreeNode<T>?>
}
