package org.toolkit4j.collection.tree

class SetTreeNode<T>
@JvmOverloads
constructor(
    override var data: T?,
    override var parent: TreeNode<T>? = null,
    override val children: MutableSet<TreeNode<T>?> = mutableSetOf(),
) :
    AbstractTreeNode<T>(data, parent) {
}