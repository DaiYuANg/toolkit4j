package org.toolkit4j.collection.pageable;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;

/**
 * Paging over a {@link Set} using the backing collection's iterator order.
 * For {@link java.util.HashSet}, that order is unspecified but stable until the set is modified.
 * Use {@link java.util.LinkedHashSet} if you need a predictable order.
 */
@Data
@RequiredArgsConstructor
public class PageableSet<T> implements PageableCollection<T, Set<T>> {

  private final Set<T> set;
  private int pageNo = 1;
  private int pageSize = 10;

  @Override
  public Set<T> page(int pageNo, int pageSize) {
    checkPageArgument(pageNo, pageSize);
    val fromIndex = (pageNo - 1) * pageSize;
    val total = set.size();
    if (fromIndex >= total) {
      return emptySet();
    }
    val toIndex = Math.min(fromIndex + pageSize, total);
    return copyIteratorRange(fromIndex, toIndex - fromIndex);
  }

  @Override
  public int totalPage(int pageSize) {
    return (int) Math.ceil((double) set.size() / pageSize);
  }

  @Override
  public int current() {
    return pageNo;
  }

  @Override
  public int totalSize() {
    return set.size();
  }

  @Override
  public boolean hasNextPage() {
    return pageNo < totalPage(pageSize);
  }

  @Override
  public boolean hasPreviousPage() {
    return pageNo > 1;
  }

  @Override
  public int getNextPage() {
    return hasNextPage() ? pageNo + 1 : pageNo;
  }

  @Override
  public int getPreviousPage() {
    return hasPreviousPage() ? pageNo - 1 : pageNo;
  }

  @Override
  public Stream<T> stream() {
    return set.stream();
  }

  @Override
  public Set<T> slice(int fromIndex, int toIndex) {
    val size = set.size();
    if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
      throw new IndexOutOfBoundsException("fromIndex=%d, toIndex=%d, size=%d".formatted(fromIndex, toIndex, size));
    }
    if (fromIndex == toIndex) {
      return emptySet();
    }
    return copyIteratorRange(fromIndex, toIndex - fromIndex);
  }

  /**
   * Copies {@code length} elements starting after skipping {@code skip} iterator steps.
   * Uses the same order as {@link Set#iterator()}.
   */
  private Set<T> copyIteratorRange(int skip, int length) {
    if (length <= 0) {
      return emptySet();
    }
    Iterator<T> it = set.iterator();
    for (int i = 0; i < skip; i++) {
      if (!it.hasNext()) {
        throw new IndexOutOfBoundsException("skip=%d past end of set (size=%d)".formatted(skip, set.size()));
      }
      it.next();
    }
    HashSet<T> out = new HashSet<>(Math.max(length, 16));
    for (int n = 0; n < length; n++) {
      if (!it.hasNext()) {
        throw new IndexOutOfBoundsException(
          "expected %d elements from offset %d but iterator ended early (size=%d)".formatted(length, skip, set.size())
        );
      }
      out.add(it.next());
    }
    return out;
  }
}
