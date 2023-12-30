package org.nectar.collection.tree

import java.util.Spliterator
import java.util.Spliterators
import java.util.function.Consumer
import java.util.stream.Stream
import java.util.stream.StreamSupport

abstract class AbstractTreeNode<T>
    @JvmOverloads
    constructor(
        override var data: T? = null,
        override var parent: TreeNode<T>? = null,
    ) :
    TreeNode<T> {
        override fun addChild(child: TreeNode<T>?) {
            child?.parent = (this)
            this.children.add(child)
        }

        override fun addChildren(children: Collection<TreeNode<T>?>?) {
            children?.let { this.children.addAll(it) }
        }

        override fun removeChild(child: TreeNode<T>?) {
            child?.parent = null
            this.children.remove(child)
        }

        override fun walk(action: Consumer<TreeNode<T>?>) {
            walkRecursive(this, action, false)
        }

        override fun parallelWalk(action: Consumer<TreeNode<T>?>) {
            walkRecursive(this, action, true)
        }

        private fun walkRecursive(
            node: TreeNode<T>,
            action: Consumer<TreeNode<T>?>,
            parallel: Boolean,
        ) {
            action.accept(node)
            if (node.children.isEmpty()) return
            val stream = if (parallel) node.children.parallelStream() else node.children.stream()
            stream.forEach { child: TreeNode<T>? ->
                child?.let {
                    walkRecursive(
                        it,
                        action,
                        parallel,
                    )
                }
            }
        }

        override fun iterator(): Iterator<TreeNode<T>?> {
            return TreeIterator(this)
        }

        private fun internalStream(parallel: Boolean): Stream<TreeNode<T>?> {
            val spliterator = Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED)
            return StreamSupport.stream(spliterator, parallel)
        }

        override fun stream(): Stream<TreeNode<T>?> {
            return internalStream(false)
        }

        override fun parallelStream(): Stream<TreeNode<T>?> {
            return internalStream(true)
        }
    }
