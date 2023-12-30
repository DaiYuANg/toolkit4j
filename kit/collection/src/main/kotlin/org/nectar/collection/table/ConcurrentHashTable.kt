package org.nectar.collection.table

import com.google.common.collect.Table
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ConcurrentHashTable<R : Any, C : Any, V : Any> : ConcurrentTable<R, C, V> {
    private val internal: ConcurrentHashMap<R, ConcurrentHashMap<C, V>> by lazy {
        ConcurrentHashMap()
    }

    override fun contains(
        rowKey: R,
        columnKey: C,
    ): Boolean {
        return Optional.ofNullable<ConcurrentMap<C, V>?>(internal[rowKey])
            .map { rowMap: ConcurrentMap<C, V>? ->
                rowMap!!.containsKey(
                    columnKey,
                )
            }
            .orElse(false)
    }

    override fun containsRow(rowKey: R): Boolean {
        return internal.containsKey(rowKey)
    }

    override fun containsColumn(columnKey: C): Boolean {
        return internal.values.stream().anyMatch { rowMap: ConcurrentMap<C, V> ->
            rowMap.containsKey(
                columnKey,
            )
        }
    }

    override fun containsValue(value: V): Boolean {
        return internal.values.stream().anyMatch { rowMap: ConcurrentMap<C, V> ->
            rowMap.containsValue(
                value,
            )
        }
    }

    override fun get(
        rowKey: R,
        columnKey: C,
    ): V? {
        return Optional.ofNullable<ConcurrentMap<C, V>?>(internal[rowKey])
            .map { row: ConcurrentMap<C, V>? -> row!![columnKey] }
            .orElse(null)
    }

    override val isEmpty: Boolean
        get() = internal.isEmpty()

    override fun size(): Int {
        return internal.values.stream().mapToInt { obj: ConcurrentMap<C, V> -> obj.size }.sum()
    }

    override fun equals(other: Any?): Boolean {
        TODO("Not yet implemented")
    }

    override fun hashCode(): Int {
        TODO("Not yet implemented")
    }

    override fun clear() {
        internal.clear()
    }

    override fun cellSet(): Set<Cell<R, C, V>?> {
        return internal.flatMap { (rowKey, rowMap) ->
            rowMap.entries.map { (columnKey, value) -> Cell(rowKey, columnKey, value) }
        }.toSet()
    }

    override fun rowKeySet(): Set<R> {
        return internal.keys.toSet()
    }

    override fun columnKeySet(): Set<C> {
        val columnKeys: MutableSet<C> = HashSet()
        internal.values.forEach { rowMap ->
            columnKeys.addAll(rowMap.keys)
        }
        return columnKeys
    }

    override fun values(): Collection<V> {
        return internal.values.flatMap { it.values }.toList()
    }

    override fun rowMap(): Map<R, Map<C, V>?> {
        return internal.mapValues { (_, rowMap) -> rowMap.toMap() }
    }

    override fun columnMap(): Map<C, Map<R, V>?> {
        TODO("Not yet implemented")
    }

    override fun column(columnKey: C): Map<R, V>? {
        TODO("Not yet implemented")
    }

    override fun row(rowKey: R): Map<C, V>? {
        TODO("Not yet implemented")
    }

    override fun remove(
        rowKey: R,
        columnKey: C,
    ): V {
        return internal[rowKey]?.remove(columnKey) ?: throw NoSuchElementException("Key not found")
    }

    override fun putAll(table: Table<out R, out C, out V>) {
        table.cellSet().forEach { cell ->
            internal.compute(cell.rowKey) { _, rowMap ->
                (rowMap ?: ConcurrentHashMap()).apply {
                    put(cell.columnKey, cell.value)
                }
            }
        }
    }

    override fun put(
        rowKey: R,
        columnKey: C,
        value: V,
    ): V {
        return internal.compute(rowKey) { _, rowMap ->
            (rowMap ?: ConcurrentHashMap<C, V>()).apply {
                put(columnKey, value)
            }
        }?.put(columnKey, value) ?: throw NoSuchElementException("Key not found")
    }
}
