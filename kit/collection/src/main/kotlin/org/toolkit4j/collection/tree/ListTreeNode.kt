package org.toolkit4j.collection.tree

data class ListTreeNode<T : Any>
@JvmOverloads
constructor(
    override var data: T,
    override var parent: TreeNode<T>? = null,
) :
    AbstractTreeNode<T>(data, parent) {
    override val children: MutableCollection<out TreeNode<T>?> by lazy {
        mutableListOf()
    }

    companion object {
        fun <T : Any> buildTree(
            rootsData: Collection<T>,
            isRootPredicate: (T) -> Boolean,
        ): AbstractTreeNode<out Any?> {
            val rootNodes = mutableListOf<ListTreeNode<T>?>()

            val nodesMap = mutableMapOf<T, ListTreeNode<T>>()

            rootsData.forEach { data ->
                val node = ListTreeNode(data)
                nodesMap[data] = node

                if (isRootPredicate(data)) {
                    rootNodes.add(node)
                }

                // 如果适用，链接父节点
                node.parent = nodesMap[data]?.parent
            }

            nodesMap.values.forEach { node ->
                val parentData = node.parent?.data
                if (parentData != null) {
                    nodesMap[parentData]?.addChild(node)
                }
            }

            return if (rootNodes.size == 1) {
                rootNodes[0]
            } else {
                // 如果有多个根节点，创建一个虚拟根节点来容纳它们
                val virtualRoot = VirtualRootTreeNode(data = Object(), children = rootNodes)
                virtualRoot
            }
        }
    }
}


