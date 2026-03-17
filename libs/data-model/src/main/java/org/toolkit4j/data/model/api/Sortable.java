package org.toolkit4j.data.model.api;

import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public interface Sortable {
  /**
   * 顺序值，数值越小越靠前
   */
  default int getOrder() {
    return Integer.MAX_VALUE;
  }

  /**
   * 静态方法：根据 effectiveOrder 排序
   */
  @Contract("_ -> param1")
  static <T extends Sortable> @NotNull List<T> sort(@NotNull List<T> list) {
    list.sort(comparator());
    return list;
  }

  /**
   * 静态方法：返回 Comparator，可用于 Stream.sorted()
   */
  @Contract(pure = true)
  static <T extends Sortable> @NotNull Comparator<T> comparator() {
    return Comparator.comparingInt(Sortable::getOrder);
  }

  /**
   * 静态方法：根据 effectiveOrder 排序 Stream
   */
  static <T extends Sortable> Stream<T> sort(@NotNull Stream<T> stream) {
    return stream.sorted(comparator());
  }
}
