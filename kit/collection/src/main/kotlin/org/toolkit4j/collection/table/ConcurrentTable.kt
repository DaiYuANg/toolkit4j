// (C)2023
package org.toolkit4j.collection.table

import com.google.common.collect.Table
import com.google.errorprone.annotations.CanIgnoreReturnValue
import com.google.errorprone.annotations.CompatibleWith
import java.util.Optional

interface ConcurrentTable<R, C, V> {
    /**
     * Returns `true` if the table contains a mapping with the specified row and column keys.
     *
     * @param rowKey    key of row to search for
     * @param columnKey key of column to search for
     */
    fun contains(
        @CompatibleWith("R") rowKey: R,
        @CompatibleWith("C") columnKey: C,
    ): Boolean

    /**
     * Returns `true` if the table contains a mapping with the specified row key.
     *
     * @param rowKey key of row to search for
     */
    fun containsRow(
        @CompatibleWith("R") rowKey: R,
    ): Boolean

    /**
     * Returns `true` if the table contains a mapping with the specified column.
     *
     * @param columnKey key of column to search for
     */
    fun containsColumn(
        @CompatibleWith("C") columnKey: C,
    ): Boolean

    /**
     * Returns `true` if the table contains a mapping with the specified value.
     *
     * @param value value to search for
     */
    fun containsValue(
        @CompatibleWith("V") value: V,
    ): Boolean

    /**
     * Returns the value corresponding to the given row and column keys, or `null` if no such
     * mapping exists.
     *
     * @param rowKey    key of row to search for
     * @param columnKey key of column to search for
     */
    fun get(
        @CompatibleWith("R") rowKey: R,
        @CompatibleWith("C") columnKey: C,
    ): V?

    fun getOptional(
        @CompatibleWith("R") rowKey: R,
        @CompatibleWith("C") columnKey: C,
    ): Optional<V & Any> {
        return Optional.ofNullable(get(rowKey, columnKey))
    }

    /**
     * Returns `true` if the table contains no mappings.
     */
    val isEmpty: Boolean

    /**
     * Returns the number of row key / column key / value mappings in the table.
     */
    fun size(): Int

    /**
     * Compares the specified object with this table for equality. Two tables are equal when their
     * cell views, as returned by [.cellSet], are equal.
     */
    override fun equals(other: Any?): Boolean

    /**
     * Returns the hash code for this table. The hash code of a table is defined as the hash code of
     * its cell view, as returned by [.cellSet].
     */
    override fun hashCode(): Int

    // Mutators

    /**
     * Removes all mappings from the table.
     */
    fun clear()

    /**
     * Associates the specified value with the specified keys. If the table already contained a
     * mapping for those keys, the old value is replaced with the specified value.
     *
     * @param rowKey    row key that the value should be associated with
     * @param columnKey column key that the value should be associated with
     * @param value     value to be associated with the specified keys
     * @return the value previously associated with the keys, or `null` if no mapping existed
     * for the keys
     */
    @CanIgnoreReturnValue
    fun put(
        rowKey: R,
        columnKey: C,
        value: V,
    ): V

    /**
     * Copies all mappings from the specified table to this table. The effect is equivalent to calling
     * [.put] with each row key / column key / value mapping in `table`.
     *
     * @param table the table to add to this table
     */
    fun putAll(table: Table<out R, out C, out V>)

    /**
     * Removes the mapping, if any, associated with the given keys.
     *
     * @param rowKey    row key of mapping to be removed
     * @param columnKey column key of mapping to be removed
     * @return the value previously associated with the keys, or `null` if no such value existed
     */
    @CanIgnoreReturnValue
    fun remove(
        @CompatibleWith("R") rowKey: R,
        @CompatibleWith("C") columnKey: C,
    ): V

    // Views

    /**
     * Returns a view of all mappings that have the given row key. For each row key / column key /
     * value mapping in the table with that row key, the returned map associates the column key with
     * the value. If no mappings in the table have the provided row key, an empty map is returned.
     *
     *
     * Changes to the returned map will update the underlying table, and vice versa.
     *
     * @param rowKey key of row to search for in the table
     * @return the corresponding map from column keys to values
     */
    fun row(rowKey: R): Map<C, V>?

    /**
     * Returns a view of all mappings that have the given column key. For each row key / column key /
     * value mapping in the table with that column key, the returned map associates the row key with
     * the value. If no mappings in the table have the provided column key, an empty map is returned.
     *
     *
     * Changes to the returned map will update the underlying table, and vice versa.
     *
     * @param columnKey key of column to search for in the table
     * @return the corresponding map from row keys to values
     */
    fun column(columnKey: C): Map<R, V>?

    /**
     * Returns a set of all row key / column key / value triplets. Changes to the returned set will
     * update the underlying table, and vice versa. The cell set does not support the `add` or
     * `addAll` methods.
     *
     * @return set of table cells consisting of row key / column key / value triplets
     */
    fun cellSet(): Set<Cell<R, C, V>?>?

    /**
     * Returns a set of row keys that have one or more values in the table. Changes to the set will
     * update the underlying table, and vice versa.
     *
     * @return set of row keys
     */
    fun rowKeySet(): Set<R>?

    /**
     * Returns a set of column keys that have one or more values in the table. Changes to the set will
     * update the underlying table, and vice versa.
     *
     * @return set of column keys
     */
    fun columnKeySet(): Set<C>

    /**
     * Returns a collection of all values, which may contain duplicates. Changes to the returned
     * collection will update the underlying table, and vice versa.
     *
     * @return collection of values
     */
    fun values(): Collection<V>

    /**
     * Returns a view that associates each row key with the corresponding map from column keys to
     * values. Changes to the returned map will update this table. The returned map does not support
     * `put()` or `putAll()`, or `setValue()` on its entries.
     *
     *
     * In contrast, the maps returned by `rowMap().get()` have the same behavior as those
     * returned by [.row]. Those maps may support `setValue()`, `put()`, and `putAll()`.
     *
     * @return a map view from each row key to a secondary map from column keys to values
     */
    fun rowMap(): Map<R, Map<C, V>?>?

    /**
     * Returns a view that associates each column key with the corresponding map from row keys to
     * values. Changes to the returned map will update this table. The returned map does not support
     * `put()` or `putAll()`, or `setValue()` on its entries.
     *
     *
     * In contrast, the maps returned by `columnMap().get()` have the same behavior as those
     * returned by [.column]. Those maps may support `setValue()`, `put()`, and
     * `putAll()`.
     *
     * @return a map view from each column key to a secondary map from row keys to values
     */
    fun columnMap(): Map<C, Map<R, V>?>?
}
