package org.toolkit4j.fp.executor;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

@UtilityClass
public class IdempotentExecutor<K, V> {
  /**
   * 通用幂等包装器。
   *
   * @param key    幂等检查用的 key（用于日志追踪）
   * @param lookup 查询已有结果的函数，返回 Optional.empty 表示无缓存
   * @param create 创建新结果的函数，只有在 lookup 未命中时执行
   * @param <K>    key 类型
   * @param <T>    结果类型
   * @return 已存在或新创建的结果
   */
  public static <K, T> T execute(K key, @NotNull Supplier<Optional<T>> lookup, @NotNull Supplier<T> create) {
    val optionalResult = lookup.get();
    return optionalResult.orElseGet(create);
  }
}
