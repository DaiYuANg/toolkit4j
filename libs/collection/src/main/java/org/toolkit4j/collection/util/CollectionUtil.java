package org.toolkit4j.collection.util;

import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

@UtilityClass
public class CollectionUtil {
  @SafeVarargs
  public static <T> Collection<T> merge(Collection<T>... collections) {
    return merge(false, collections);
  }

  @SafeVarargs
  public static <T> Collection<T> mergeDistinct(Collection<T>... collections) {
    return merge(true, collections);
  }

  @SafeVarargs
  private static <T> Collection<T> merge(boolean distinct, Collection<T>... collections) {
    if (collections == null || collections.length == 0) {
      return emptyList();
    }

    val stream = Stream.of(collections)
      .filter(Objects::nonNull)
      .flatMap(Collection::stream);

    return (distinct ? stream.distinct() : stream).collect(Collectors.toList());
  }

  /**
   * 交集：求所有集合中的公共元素
   */
  @SafeVarargs
  public static <T> Collection<T> intersection(Collection<T>... collections) {
    if (collections == null || collections.length == 0) {
      return emptyList();
    }

    val hasEmpty = Stream.of(collections)
      .filter(Objects::nonNull)
      .anyMatch(Collection::isEmpty);

    return hasEmpty ? Collections.emptyList() : Stream.of(collections)
      .filter(Objects::nonNull)
      .filter(c -> !c.isEmpty())
      .map(HashSet::new)
      .reduce((set1, set2) -> {
        set1.retainAll(set2);
        return set1;
      })
      .map(ArrayList::new)
      .orElse(new ArrayList<>());
  }
}
