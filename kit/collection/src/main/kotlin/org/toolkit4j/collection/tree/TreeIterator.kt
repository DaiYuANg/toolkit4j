// (C)2023
package org.toolkit4j.collection.tree

class TreeIterator<T>(root: TreeNode<T>) : MutableIterator<TreeNode<T>> {
    private var current: TreeNode<T>?
    private var childrenIterator: MutableIterator<TreeNode<T>?>

    init {
        this.current = root
        this.childrenIterator = root.children.iterator()
    }

    override fun hasNext(): Boolean {
        return current != null
    }

    override fun next(): TreeNode<T> {
        checkNotNull(current) { "No more elements in the tree" }

        val nextNode: TreeNode<T> = current as TreeNode<T>

        if (childrenIterator.hasNext()) {
            current = childrenIterator.next()
            childrenIterator = current!!.children.iterator()
        } else {
            current = null
        }

        return nextNode
    }

    override fun remove() {
        TODO("Not yet implemented")
    }
}
