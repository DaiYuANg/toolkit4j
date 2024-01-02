package org.toolkit4j.collection.tree

class VirtualRootTreeNode @JvmOverloads constructor(
    override var data: Any = Object(),
    override var parent: TreeNode<Any>? = null,
    override val children: MutableCollection<out TreeNode<Any>?>,
) : AbstractTreeNode<Any>(
    data, parent
)