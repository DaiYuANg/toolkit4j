package org.toolkit4j.collection.page

interface PageableCollection<T, C : Collection<T>> {
    var pageNo: Int

    var pageSize: Int

    fun page(
        pageNo: Int,
        pageSize: Int,
    ): C

    fun checkPageArgument(pageNo: Int,pageSize: Int){
        require(pageNo > 0) { "Page number should be non-negative." }
        require(pageSize > 0) { "Page size should be positive." }
    }

    fun totalPage(size: Int): Int

    fun current(): Int

    fun totalSize(): Int

    fun hasNextPage(): Boolean

    fun hasPreviousPage(): Boolean

    fun getNextPage(): Int

    fun getPreviousPage(): Int
}
