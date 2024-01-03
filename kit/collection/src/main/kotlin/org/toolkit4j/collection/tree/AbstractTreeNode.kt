package org.toolkit4j.collection.tree

import java.util.Spliterator
import java.util.Spliterators
import java.util.function.Consumer
import java.util.stream.Stream
import java.util.stream.StreamSupport

abstract class AbstractTreeNode<T : Any, out C : MutableCollection<T>>
@JvmOverloads
constructor(
    override var data: T,
    override var parent: TreeNode<T, @UnsafeVariance C>? = null,
) :
    TreeNode<T, C> {
    override fun addChild(child: TreeNode<T, @UnsafeVariance C>?) {
        child?.parent = (this)
        this.children.add(child)
    }

    override fun addChildren(children: Collection<TreeNode<T, @UnsafeVariance C>?>) {
        children.let { this.children.addAll(it) }
    }

    override fun removeChild(child: TreeNode<T, @UnsafeVariance C>) {
        child.parent = null
        this.children.remove(child)
    }

    override fun walk(action: Consumer<TreeNode<T, @UnsafeVariance C>?>) {
        walkRecursive(this, action, false)
    }

    override fun parallelWalk(action: Consumer<TreeNode<T, @UnsafeVariance C>?>) {
        walkRecursive(this, action, true)
    }

    private fun walkRecursive(
        node: TreeNode<T, C>,
        action: Consumer<TreeNode<T, C>?>,
        parallel: Boolean,
    ) {
        action.accept(node)
        if (node.children.isEmpty()) return
        val stream = if (parallel) node.children.parallelStream() else node.children.stream()
        stream.forEach { child: TreeNode<T, C>? ->
            child?.let {
                walkRecursive(
                    it,
                    action,
                    parallel,
                )
            }
        }
    }

    override fun iterator(): Iterator<TreeNode<T, C>?> {
        return TreeIterator(this)
    }

    private fun internalStream(parallel: Boolean): Stream<TreeNode<T, C>?> {
        val spliterator = Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED)
        return StreamSupport.stream(spliterator, parallel)
    }

    override fun stream(): Stream<TreeNode<T, @UnsafeVariance C>?> {
        return internalStream(false)
    }

    override fun parallelStream(): Stream<TreeNode<T, @UnsafeVariance C>?> {
        return internalStream(true)
    }
}
