package org.toolkit4j.collection.pageable

import java.util.*
import kotlin.math.min

data class PageableSet<T> @JvmOverloads constructor(
    private val data: SortedSet<T> = TreeSet(),
    override var pageNo: Int = 1,
    override var pageSize: Int = 10
) : PageableCollection<T, SortedSet<T>>, SortedSet<T> by data {

    init {
        checkPageArgument(pageNo, pageSize)
    }

    override fun page(pageNo: Int, pageSize: Int): SortedSet<T> {
        checkPageArgument(pageNo, pageSize)

        val adjustedPageNo = maxOf(1, pageNo)

        val startIndex = (adjustedPageNo - 1) * pageSize
        val endIndex = adjustedPageNo * pageSize

        val actualEndIndex = min(endIndex, data.size)

        return data.subSet(data.elementAt(startIndex), data.elementAt(actualEndIndex))
    }

    override fun totalPage(size: Int): Int {
        return (data.size + size - 1) / size
    }

    override fun current(): Int {
        return pageNo
    }

    override fun totalSize(): Int {
        return data.size
    }

    override fun hasNextPage(): Boolean {
        return pageNo < totalPage(pageSize)
    }

    override fun hasPreviousPage(): Boolean {
        return pageNo > 1
    }

    override fun getNextPage(): Int {
        return if (hasNextPage()) pageNo + 1 else pageNo
    }

    override fun getPreviousPage(): Int {
        return if (hasPreviousPage()) pageNo - 1 else pageNo
    }
}