/* (C)2023*/
package org.toolkit4j.collection.pageable;

import lombok.val;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PageableListTest {

  private PageableList<String> pageable;
  private final Faker faker = new Faker();

  @BeforeEach
  void setup() {
    List<String> fakeData = IntStream.range(0, 1000)
      .mapToObj(i -> faker.camera().brand())
      .toList();
    pageable = new PageableList<>(new ArrayList<>(fakeData));
  }

  @Test
  void testPage() {
    val firstPage = pageable.page(1, 100);
    assertEquals(100, firstPage.size());
    assertEquals(1000, pageable.totalSize());
    assertEquals(10, pageable.totalPage(100));
  }

  @Test
  void testInvalidPageNo() {
    assertThrows(IllegalArgumentException.class, () -> pageable.page(0, 10));
  }

  @Test
  void testInvalidPageSize() {
    assertThrows(IllegalArgumentException.class, () -> pageable.page(1, 0));
  }

  @Test
  void testStream() {
    val count = pageable.stream().count();
    assertEquals(1000, count);
  }

  @Test
  void testFirstAndLast() {
    assertTrue(pageable.first().isPresent());
    assertTrue(pageable.last().isPresent());
    assertEquals(1000, pageable.stream().count());

    val empty = new PageableList<>(List.<String>of());
    assertTrue(empty.first().isEmpty());
    assertTrue(empty.last().isEmpty());
  }

  @Test
  void testSlice() {
    val slice = pageable.slice(10, 20);
    assertEquals(10, slice.size());
  }
}
