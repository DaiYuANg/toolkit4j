package org.toolkit4j.core.util;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class NullOrEmpty {

    public boolean isNullOrEmpty(Object obj) {
        if (obj instanceof String string) {
            return isNullOrEmpty(string);
        }
        return Objects.isNull(obj);
    }

    public boolean isNullOrEmpty(CharSequence cs) {
        return Objects.isNull(cs) || cs.isEmpty();
    }

    public boolean isNullOrEmpty(Iterable<?> iterable) {
        return Objects.isNull(iterable) || !iterable.iterator().hasNext();
    }
}
