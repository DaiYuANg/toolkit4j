package org.nectar.collection.page

/**
 *
 */
data class PageableArrayList<T>
    @JvmOverloads
    constructor(
        private val data: MutableList<T> = ArrayList(),
        override var pageNo: Int = 1,
        override var pageSize: Int = 10,
    ) :
    PageableCollection<T, MutableList<T>>, MutableList<T> by data {
        override fun page(
            pageNo: Int,
            pageSize: Int,
        ): MutableList<T> {
            val startIndex = (pageNo - 1) * pageSize
            val endIndex = (pageNo * pageSize).coerceAtMost(data.size)
            return data.subList(startIndex, endIndex).toMutableList().also { updatePageInfo(pageNo, pageSize) }
        }

        override fun totalPage(size: Int): Int {
            return (data.size + size - 1) / size
        }

        override fun current(): Int {
            return data.size
        }

        override fun totalSize(): Int {
            return data.size
        }

        override fun hasNextPage(): Boolean {
            TODO("Not yet implemented")
        }

        override fun hasPreviousPage(): Boolean {
            TODO("Not yet implemented")
        }

        override fun getNextPage(): Int {
            TODO("Not yet implemented")
        }

        override fun getPreviousPage(): Int {
            TODO("Not yet implemented")
        }

        private fun updatePageInfo(
            page: Int,
            size: Int,
        ) {
            this.pageNo = page
            this.pageSize = size
        }
    }
