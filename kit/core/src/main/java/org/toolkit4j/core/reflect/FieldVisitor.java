package org.toolkit4j.core.reflect;

import lombok.Builder;
import lombok.Singular;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.toolkit4j.core.util.NullOrEmpty;

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

  @Contract(" -> new")
  public static @NotNull FieldVisitorBuilder builder() {
    return new SelfBuilder();
  }

  /**
   * Custom builder class
   */
  private static class SelfBuilder extends FieldVisitorBuilder {
    @Override
    public @NotNull FieldVisitor build() {
      return super.build();
    }
  }

  @SneakyThrows
  public void visitObject(@NotNull final Object obj, final BiConsumer<Field, Object> visitor) {
    val clazz = obj.getClass();
//        NullOrEmpty.isNullOrEmpty(filters);
    var fields = Arrays.stream(clazz.getDeclaredFields());
    for (Predicate<Field> filter : filters) {
      fields = fields.filter(filter);
    }
    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);
      val fieldValue = field.get(obj);
      val dto = new FieldProcessDto(field, fieldValue, visitor);
      fieldProcessors
        .stream()
        .filter(processor -> processor.isApplicable(field))
        .forEach(processor -> processor.process(field, fieldValue, visitor));
    }
  }
}