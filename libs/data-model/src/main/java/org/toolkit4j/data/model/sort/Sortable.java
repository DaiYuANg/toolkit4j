package org.toolkit4j.data.model.sort;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface Sortable {

  /** Order value; smaller values come first. */
  default int getOrder() {
    return Integer.MAX_VALUE;
  }

  /**
   * Final order value. Kept separate so future non-framework metadata can be layered without
   * breaking callers.
   */
  default int effectiveOrder() {
    return getOrder();
  }

  @Contract("_ -> param1")
  static <T extends Sortable> @NotNull List<T> sort(@NotNull List<T> list) {
    list.sort(comparator());
    return list;
  }

  @Contract(pure = true)
  static <T extends Sortable> @NotNull Comparator<T> comparator() {
    return Comparator.comparingInt(Sortable::effectiveOrder);
  }

  static <T extends Sortable> @NotNull Stream<T> sort(@NotNull Stream<T> stream) {
    return stream.sorted(comparator());
  }
}
