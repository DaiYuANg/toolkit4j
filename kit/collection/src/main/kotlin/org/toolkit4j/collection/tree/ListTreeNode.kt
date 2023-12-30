package org.toolkit4j.collection.tree

data class ListTreeNode<T>
    @JvmOverloads
    constructor(
        override var data: T? = null,
        override var parent: TreeNode<T>? = null,
        override val children: MutableCollection<TreeNode<T>?> = mutableListOf(),
    ) :
    AbstractTreeNode<T>(data, parent)
