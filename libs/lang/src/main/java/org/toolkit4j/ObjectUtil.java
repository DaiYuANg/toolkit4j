package org.toolkit4j;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public class ObjectUtil {
  public boolean isEmpty(Object obj) {
    switch (obj) {
      case null -> {
        return true;
      }
      case Optional<?> optional -> {
        return optional.isEmpty();
      }
      case CharSequence cs -> {
        return cs.isEmpty();
      }
      case Collection<?> collection -> {
        return collection.isEmpty();
      }
      case Map<?, ?> map -> {
        return map.isEmpty();
      }
      default -> {
      }
    }

    if (obj.getClass().isArray()) {
      if (obj instanceof Object[] arr) {
        return arr.length == 0;
      }

      // 处理基本类型数组
      int length = Array.getLength(obj);
      return length == 0;
    }

    return false;
  }
}