package org.toolkit4j.sensitive;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class SensitiveBeanSerializerModifier extends BeanSerializerModifier {
  @Override
  public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                   BeanDescription beanDesc,
                                                   @NotNull List<BeanPropertyWriter> beanProperties) {
    beanProperties.forEach(writer -> {
      Field field = getField(beanDesc.getBeanClass(), writer.getName());
      if (field != null && field.isAnnotationPresent(Sensitive.class)) {
        Sensitive annotation = field.getAnnotation(Sensitive.class);
        writer.assignSerializer(new SensitiveSerializer(requireNonNull(annotation).value()));
      }
    });
    return beanProperties;
  }

  @Contract(pure = true)
  private @Nullable Field getField(@NotNull Class<?> clazz, String fieldName) {
    try {
      return clazz.getDeclaredField(fieldName);
    } catch (NoSuchFieldException e) {
      return null;
    }
  }
}