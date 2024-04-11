package org.toolkit4j.core.reflect;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

public interface FieldProcessor {

  boolean isApplicable(Field field);

  void process(final Field field, final Object value, final BiConsumer<Field, Object> consumer);
}
