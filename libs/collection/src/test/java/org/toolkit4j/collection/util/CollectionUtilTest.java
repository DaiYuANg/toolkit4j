package org.toolkit4j.collection.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.Test;

class CollectionUtilTest {
  @Test
  void testMerge_multipleCollections() {
    List<Integer> list1 = Arrays.asList(1, 2, 3);
    List<Integer> list2 = Arrays.asList(4, 5);
    List<Integer> list3 = List.of(6);

    val merged = CollectionUtil.merge(list1, list2, list3);

    assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), merged);
  }

  @Test
  void testMerge_withNullAndEmptyCollections() {
    List<Integer> list1 = Arrays.asList(1, 2);
    List<Integer> list2 = null;
    List<Integer> list3 = Collections.emptyList();

    val merged = CollectionUtil.merge(list1, list2, list3);

    assertEquals(Arrays.asList(1, 2), merged);
  }

  @Test
  void testMerge_emptyInput() {
    val merged = CollectionUtil.<Integer>merge();
    assertTrue(merged.isEmpty());

    val mergedNull = CollectionUtil.merge((Collection<Integer>[]) null);
    assertTrue(mergedNull.isEmpty());
  }

  @Test
  void testMergeDistinct_removesDuplicates() {
    List<Integer> list1 = Arrays.asList(1, 2, 3, 2);
    List<Integer> list2 = Arrays.asList(3, 4, 1);

    val mergedDistinct = CollectionUtil.mergeDistinct(list1, list2);

    // 顺序按出现顺序，去重
    assertEquals(Arrays.asList(1, 2, 3, 4), mergedDistinct);
  }

  @Test
  void testIntersection_commonElements() {
    List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
    List<Integer> list2 = Arrays.asList(3, 4, 5, 6);
    List<Integer> list3 = Arrays.asList(4, 7, 8);

    val intersection = CollectionUtil.intersection(list1, list2, list3);

    assertEquals(Collections.singletonList(4), intersection);
  }

  @Test
  void testIntersection_withEmptyAndNullCollections() {
    List<Integer> list1 = Arrays.asList(1, 2);
    List<Integer> list2 = Collections.emptyList();
    List<Integer> list3 = null;

    val intersection = CollectionUtil.intersection(list1, list2, null);

    assertTrue(intersection.isEmpty());
  }

  @Test
  void testIntersection_noCommonElements() {
    List<Integer> list1 = Arrays.asList(1, 2);
    List<Integer> list2 = Arrays.asList(3, 4);

    val intersection = CollectionUtil.intersection(list1, list2);

    assertTrue(intersection.isEmpty());
  }
}
