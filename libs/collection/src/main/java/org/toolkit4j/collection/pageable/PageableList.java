package org.toolkit4j.collection.pageable;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Data
public class PageableList<T> implements PageableCollection<T, List<T>> {
  private int pageNo;
  private int pageSize;
  private final List<T> data;

  @Override
  public List<T> page(int pageNo, int pageSize) {
    checkPageArgument(pageNo, pageSize);
    val fromIndex = (pageNo - 1) * pageSize;
    val toIndex = Math.min(fromIndex + pageSize, data.size());
    return new ArrayList<>(data.subList(fromIndex, toIndex));
  }

  @Override
  public int totalPage(int size) {
    return (int) Math.ceil((double) data.size() / size);
  }

  @Override
  public int current() {
    return pageNo;
  }

  @Override
  public int totalSize() {
    return data.size();
  }

  @Override
  public boolean hasNextPage() {
    return pageNo * pageSize < data.size();
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
