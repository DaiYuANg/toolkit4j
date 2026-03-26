package org.toolkit4j.data.model.sort;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SortableTest {

  @Test
  void sort_ordersAscendingByEffectiveOrder() {
    var values = new ArrayList<>(List.of(
      new SampleSortable("third", 30),
      new SampleSortable("first", 10),
      new SampleSortable("second", 20)
    ));

    Sortable.sort(values);

    assertEquals("first", values.get(0).name());
    assertEquals("second", values.get(1).name());
    assertEquals("third", values.get(2).name());
  }

  @Test
  void sort_streamUsesSameComparator() {
    var ordered = Sortable.sort(List.of(
      new SampleSortable("third", 30),
      new SampleSortable("first", 10),
      new SampleSortable("second", 20)
    ).stream()).toList();

    assertEquals("first", ordered.get(0).name());
    assertEquals("second", ordered.get(1).name());
    assertEquals("third", ordered.get(2).name());
  }

  private record SampleSortable(String name, int order) implements Sortable {
    @Override
    public int getOrder() {
      return order;
    }
  }
}
