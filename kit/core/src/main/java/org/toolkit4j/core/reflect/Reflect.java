package org.toolkit4j.core.reflect;

import lombok.Builder;
import lombok.Singular;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.function.BiConsumer;

@Builder
public class Reflect {

    @Singular
    private final Set<FieldProcessor> fieldProcessors;

    @Contract(" -> new")
    public static @NotNull ReflectBuilder builder() {
        return new SelfBuilder();
    }

    /**
     * Cusotm  builder class
     */
    private static class SelfBuilder extends ReflectBuilder {
        @Override
        public @NotNull Reflect build() {
            return super.build();
        }
    }

    @SneakyThrows
    public void visitObject(@NotNull final Object obj, BiConsumer<Field, Object> visitor) {
        val clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            val fieldValue = field.get(obj);
            for (FieldProcessor processor : fieldProcessors) {
                processor.process(field, fieldValue, visitor);
            }
        }
    }
}