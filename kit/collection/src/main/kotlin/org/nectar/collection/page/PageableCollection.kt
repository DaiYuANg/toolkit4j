package org.nectar.collection.page

interface PageableCollection<T, C : Collection<T>> {
    var pageNo: Int

    var pageSize: Int

    fun page(
        pageNo: Int,
        pageSize: Int,
    ): C

    fun totalPage(size: Int): Int

    fun current(): Int

    fun totalSize(): Int

    fun hasNextPage(): Boolean

    fun hasPreviousPage(): Boolean

    fun getNextPage(): Int

    fun getPreviousPage(): Int
}
