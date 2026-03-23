package org.toolkit4j.collection.pageable;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public interface PageableCollection<T, C extends Collection<T>> {

  int getPageNo();

  void setPageNo(int pageNo);

  int getPageSize();

  void setPageSize(int pageSize);

  C page(int pageNo, int pageSize);

  /**
   * 校验分页参数。Validates page arguments.
   */
  default void checkPageArgument(int pageNo, int pageSize) {
    if (pageNo <= 0) {
      throw new IllegalArgumentException("Page number must be positive.");
    }
    if (pageSize <= 0) {
      throw new IllegalArgumentException("Page size must be positive.");
    }
  }

  /**
   * 在给定每页大小时，返回总页数。
   * Returns total number of pages for the given page size.
   *
   * @param pageSize 每页元素数量 / number of elements per page
   * @return 总页数 / total number of pages
   */
  int totalPage(int pageSize);

  int current();

  int totalSize();

  boolean hasNextPage();

  boolean hasPreviousPage();

  int getNextPage();

  int getPreviousPage();

  // --- Stream 与便捷方法 / Stream and convenience methods ---

  /**
   * 返回所有元素的流。Returns a stream of all elements.
   */
  Stream<T> stream();

  /**
   * 第一个元素（空集合返回 empty）。First element, or empty if collection is empty.
   */
  default Optional<T> first() {
    return stream().findFirst();
  }

  /**
   * 最后一个元素（空返回 empty；Set 为迭代顺序的最后一个）。
   * Last element, or empty if collection is empty. For Set, last in iteration order.
   */
  default Optional<T> last() {
    return stream().reduce((a, b) -> b);
  }

  /**
   * 切片 [fromIndex, toIndex)。Returns sub-collection in range [fromIndex, toIndex).
   */
  C slice(int fromIndex, int toIndex);
}