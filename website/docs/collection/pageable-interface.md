# Pageable API

What is Pageable: A set of data partitioned into fixed-sized sections, where obtaining a specific partition's data
collection is based on the index of the partition.

```mermaid
graph TD
    A[Request page of 1 and size is 10] -->|Page 1| B[返回第一页数据]
    B -->|Page 1| C[显示第一页数据]

    C -->|用户翻页| A
    A -->|请求第二页数据| D[返回第二页数据]
    D -->|Page 2| E[显示第二页数据]

    E -->|用户翻页| A
    A -->|请求第三页数据| F[返回第三页数据]
    F -->|Page 3| G[显示第三页数据]

    G -->|用户翻页| A
```

> And there is pageable able todo
```kotlin
interface PageableCollection<T, C : Collection<T>> {
    var pageNo: Int

    var pageSize: Int

    fun page(
        pageNo: Int,
        pageSize: Int,
    ): C

    fun checkPageArgument(pageNo: Int, pageSize: Int) {
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
```