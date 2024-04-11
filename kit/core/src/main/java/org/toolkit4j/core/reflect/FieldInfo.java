package org.toolkit4j.core.reflect;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

@Data
@Builder
public class FieldInfo {
  @NonNull
  private final Field field;
  @Nullable
  private final Object value;

  @NonNull
  private final BiConsumer<Field, Object> visitor;
}
