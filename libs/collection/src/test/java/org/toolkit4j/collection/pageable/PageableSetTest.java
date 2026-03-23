package org.toolkit4j.collection.pageable;

import lombok.val;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PageableSetTest {

  private PageableSet<String> pageable;
  private final Faker faker = new Faker();

  @BeforeEach
  void setup() {
    Set<String> fakeData = IntStream.range(0, 1000)
      .mapToObj(i -> faker.name().fullName())
      .collect(Collectors.toSet());
    pageable = new PageableSet<>(fakeData);
  }

  @Test
  void testPage() {
    val pageSize = 50;
    val paged = pageable.page(2, pageSize);
    assertEquals(pageSize, paged.size());
  }

  @Test
  void testZeroPageNo() {
    assertThrows(IllegalArgumentException.class, () -> pageable.page(0, 10));
  }

  @Test
  void testInvalidPageSize() {
    assertThrows(IllegalArgumentException.class, () -> pageable.page(1, 0));
  }

  @Test
  void testStream() {
    assertEquals(pageable.totalSize(), pageable.stream().count());
  }

  @Test
  void testFirstAndLast() {
    assertTrue(pageable.first().isPresent());
    assertTrue(pageable.last().isPresent());
  }

  @Test
  void testSlice() {
    val slice = pageable.slice(10, 20);
    assertEquals(10, slice.size());
  }
}
