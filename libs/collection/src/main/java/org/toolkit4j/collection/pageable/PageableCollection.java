package org.toolkit4j.collection.pageable;

import java.util.Collection;

public interface PageableCollection<T, C extends Collection<T>> {

  int getPageNo();
  void setPageNo(int pageNo);

  int getPageSize();
  void setPageSize(int pageSize);

  C page(int pageNo, int pageSize);

  default void checkPageArgument(int pageNo, int pageSize) {
    if (pageNo <= 0) {
      throw new IllegalArgumentException("Page number should be non-negative.");
    }
    if (pageSize <= 0) {
      throw new IllegalArgumentException("Page size should be positive.");
    }
  }

  int totalPage(int size);

  int current();

  int totalSize();

  boolean hasNextPage();

  boolean hasPreviousPage();

  int getNextPage();

  int getPreviousPage();
}