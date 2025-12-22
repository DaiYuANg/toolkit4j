package org.toolkit4j.collection.pageable;

import lombok.*;

import java.util.*;

import static java.util.Collections.emptySet;

@Data
@RequiredArgsConstructor
public class PageableSet<T> implements PageableCollection<T, Set<T>> {

  private final Set<T> set;
  private int pageNo = 1;
  private int pageSize = 10;

  @Override
  public Set<T> page(int pageNo, int pageSize) {
    val list = new ArrayList<>(set);
    val fromIndex = Math.max(0, (pageNo - 1) * pageSize);
    val toIndex = Math.min(list.size(), fromIndex + pageSize);
    if (fromIndex >= list.size()) return emptySet();
    return new HashSet<>(list.subList(fromIndex, toIndex));
  }

  @Override
  public int totalPage(int size) {
    return (int) Math.ceil((double) size / pageSize);
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
    return pageNo < totalPage(set.size());
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
}
