package org.toolkit4j.collection.table

import com.google.common.collect.Table

data class Cell<R, C, V>(
    private val rowKey: R,
    private val columnKey: C,
    private val value: V,
) : Table.Cell<R, C, V> {
    override fun getRowKey(): R {
        TODO("Not yet implemented")
    }

    override fun getColumnKey(): C {
        TODO("Not yet implemented")
    }

    override fun getValue(): V {
        TODO("Not yet implemented")
    }
}
