package org.toolkit4j.core.reflect;

import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

@Builder
public class FieldVisitor {

  @Singular
  private final Set<FieldProcessor> fieldProcessors;

  private final Boolean setAccessible;

  @Singular
  private final Set<Predicate<Field>> filters;

  private final Integer depth;


  @SneakyThrows
  public void visitObject(@NotNull final Object obj, final BiConsumer<Field, Object> visitor) {
    val clazz = obj.getClass();
    var fields = Arrays.stream(clazz.getDeclaredFields());
    for (Predicate<Field> filter : filters) {
      fields = fields.filter(filter);
    }
    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);
      val fieldInfo = FieldInfo
        .builder()
        .field(field)
        .value(field.get(obj))
        .visitor(visitor)
        .build();
      fieldProcessors
        .forEach(processor -> processor.accept(fieldInfo));
    }
  }
}