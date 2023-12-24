package org.nova.core

data class SourceTreeNode(
    val name: String,
    val isFile: Boolean,
    val children: MutableList<SourceTreeNode> = mutableListOf()
)