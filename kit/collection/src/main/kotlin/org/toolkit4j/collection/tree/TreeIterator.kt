// (C)2023
package org.toolkit4j.collection.tree

class TreeIterator<T, out C : MutableCollection<T>>(root: TreeNode<T,C>) : MutableIterator<TreeNode<T,C>> {
    private var current: TreeNode<T,C>?
    private var childrenIterator: MutableIterator<TreeNode<T,C>?>

    init {
        this.current = root
        this.childrenIterator = root.children.iterator()
    }

    override fun hasNext(): Boolean {
        return current != null
    }

    override fun next(): TreeNode<T,C> {
        checkNotNull(current) { "No more elements in the tree" }

        val nextNode: TreeNode<T,C> = current as TreeNode<T,C>

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
