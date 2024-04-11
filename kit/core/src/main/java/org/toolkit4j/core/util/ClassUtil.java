package org.toolkit4j.core.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@UtilityClass
public class ClassUtil {

  public final Set<Class<?>> PRIMITIVE_WRAPPER = Set.of(
    Boolean.class,
    Byte.class,
    Character.class,
    Short.class,
    Integer.class,
    Long.class,
    Float.class,
    Double.class,
    Void.class
  );

  public boolean isPrimitiveOrWrapper(@NotNull Class<?> type) {
    return type.isPrimitive() ||
      PRIMITIVE_WRAPPER.contains(type);
  }
}
