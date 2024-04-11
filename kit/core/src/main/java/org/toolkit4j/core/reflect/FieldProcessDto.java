package org.toolkit4j.core.reflect;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

@Data
@Builder
public class FieldProcessDto {
    private final Field field;
    private final Object o;
    private final BiConsumer<Field, Object> consumer;
}
