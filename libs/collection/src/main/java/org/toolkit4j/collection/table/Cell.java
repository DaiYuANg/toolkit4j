package org.toolkit4j.collection.table;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Cell<R, C, V> {
  R getRowKey();

  C getColumnKey();

  V getValue();

  /** 函数式变换值 */
  default <V2> Cell<R, C, V2> mapValue(@NotNull Function<? super V, ? extends V2> mapper) {
    return of(getRowKey(), getColumnKey(), mapper.apply(getValue()));
  }

  /** 映射为任意对象 */
  default <T> T map(@NotNull BiFunction<? super R, ? super C, ? extends T> mapper) {
    return mapper.apply(getRowKey(), getColumnKey());
  }

  /** 将 Cell 展开为三元组 */
  default <T> T mapTri(@NotNull Function3<? super R, ? super C, ? super V, ? extends T> mapper) {
    return mapper.apply(getRowKey(), getColumnKey(), getValue());
  }

  /** 构造函数 */
  @Contract(value = "_, _, _ -> new", pure = true)
  static <R, C, V> @NotNull Cell<R, C, V> of(R r, C c, V v) {
    return new Cell<>() {
      public R getRowKey() {
        return r;
      }

      public C getColumnKey() {
        return c;
      }

      public V getValue() {
        return v;
      }
    };
  }
}
