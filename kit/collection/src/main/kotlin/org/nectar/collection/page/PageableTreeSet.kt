package org.nectar.collection.page

import java.util.TreeSet

class PageableTreeSet<T>(
    private val data: TreeSet<T>,
    override var pageNo: Int, override var pageSize: Int
) : PageableCollection<T, TreeSet<T>>, Set<T> by data {
    override fun page(pageNo: Int, pageSize: Int): TreeSet<T> {
        TODO("Not yet implemented")
    }

    override fun totalPage(size: Int): Int {
        TODO("Not yet implemented")
    }

    override fun current(): Int {
        TODO("Not yet implemented")
    }

    override fun totalSize(): Int {
        TODO("Not yet implemented")
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
}