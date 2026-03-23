package org.toolkit4j.collection.pageable;

import lombok.*;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;

@Data
@RequiredArgsConstructor
public class PageableSet<T> implements PageableCollection<T, Set<T>> {

  private final Set<T> set;
  private int pageNo = 1;
  private int pageSize = 10;

  @Override
  public Set<T> page(int pageNo, int pageSize) {
    checkPageArgument(pageNo, pageSize);
    val list = new ArrayList<>(set);
    val fromIndex = Math.max(0, (pageNo - 1) * pageSize);
    val toIndex = Math.min(list.size(), fromIndex + pageSize);
    if (fromIndex >= list.size()) return emptySet();
    return new HashSet<>(list.subList(fromIndex, toIndex));
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
    val list = new ArrayList<>(set);
    if (fromIndex < 0 || toIndex > list.size() || fromIndex > toIndex) {
      throw new IndexOutOfBoundsException("fromIndex=%d, toIndex=%d, size=%d".formatted(fromIndex, toIndex, list.size()));
    }
    return new HashSet<>(list.subList(fromIndex, toIndex));
  }
}
