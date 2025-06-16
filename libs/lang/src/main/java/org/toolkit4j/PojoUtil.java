package org.toolkit4j;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Character.toUpperCase;

@UtilityClass
public class PojoUtil {
  private final Map<Class<?>, List<Getter>> GETTER_CACHE = new ConcurrentHashMap<>();

  public boolean isPojoEmpty(Object obj) {
    if (obj == null) return true;

    val clazz = obj.getClass();
    val getters = GETTER_CACHE.computeIfAbsent(clazz, PojoUtil::computeGetters);

    try {
      for (Getter getter : getters) {
        val value = getter.get(obj);
        if (!isEmptyValue(getter.type, value)) {
          return false;
        }
      }
      return true;
    } catch (Throwable e) {
      throw new RuntimeException("Failed to get property value", e);
    }
  }

  // 通过反射获取该类的所有 Getter MethodHandles
  private @NotNull List<Getter> computeGetters(final @NotNull Class<?> clazz) {
    val getters = new ArrayList<Getter>();
    val lookup = MethodHandles.lookup();

    try {
      if (clazz.isRecord()) {
        // 处理 record 类型
        for (RecordComponent rc : clazz.getRecordComponents()) {
          Method accessor = rc.getAccessor();
          MethodHandle mh = lookup.unreflect(accessor);
          getters.add(new Getter(rc.getType(), mh));
        }
      } else {
        // 普通类，优先找 public getter，没找到则直接访问字段
        for (Field field : clazz.getDeclaredFields()) {
          field.setAccessible(true);
          MethodHandle mh;
          try {
            // 尝试找 getter 方法
            val getterName = "get" + capitalize(field.getName());
            val getterMethod = clazz.getMethod(getterName);
            mh = lookup.unreflect(getterMethod);
          } catch (NoSuchMethodException e1) {
            // 对于 boolean 类型尝试 isXxx
            if (field.getType() == boolean.class || field.getType() == Boolean.class) {
              try {
                val isName = "is" + capitalize(field.getName());
                val isMethod = clazz.getMethod(isName);
                mh = lookup.unreflect(isMethod);
                getters.add(new Getter(field.getType(), mh));
                continue;
              } catch (NoSuchMethodException e2) {
                // 不存在 getter fallback 直接读字段
                mh = lookup.unreflectGetter(field);
              }
            } else {
              // 直接读字段
              mh = lookup.unreflectGetter(field);
            }
          }
          getters.add(new Getter(field.getType(), mh));
        }
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    return getters;
  }

  private static boolean isEmptyValue(Class<?> type, Object value) {
    if (value == null) return true;

    if (type.isPrimitive()) {
      if (type == boolean.class) return !(boolean) value;
      if (type == char.class) return (char) value == '\0';
      if (type == byte.class) return (byte) value == 0;
      if (type == short.class) return (short) value == 0;
      if (type == int.class) return (int) value == 0;
      if (type == long.class) return (long) value == 0L;
      if (type == float.class) return (float) value == 0f;
      if (type == double.class) return (double) value == 0d;
    }

    switch (value) {
      case Number num -> {
        return num.doubleValue() == 0d;
      }
      case Boolean b -> {
        return !b;
      }
      case Character c -> {
        return c == '\0';
      }
      case CharSequence cs -> {
        return cs.isEmpty();
      }
      case Collection<?> c -> {
        return c.isEmpty();
      }
      case Map<?, ?> m -> {
        return m.isEmpty();
      }
      default -> {
      }
    }

    if (type.isArray()) return Array.getLength(value) == 0;

    return false;
  }

  private static String capitalize(String name) {
    if (name == null || name.isEmpty()) return name;
    return toUpperCase(name.charAt(0)) + name.substring(1);
  }

  // 包装一个 getter 的抽象类（字段类型 + 调用 MethodHandle）
  private record Getter(Class<?> type, MethodHandle handle) {
    Object get(Object obj) throws Throwable {
      return handle.invoke(obj);
    }
  }
}
