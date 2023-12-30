package org.toolkit4j.collection.table

data class Cell<R, C, V>(
    private val rowKey: R,
    private val columnKey: C,
    private val value: V,
)
