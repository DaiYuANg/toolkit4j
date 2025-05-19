package org.toolkit4j.sensitive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 1. 脱敏注解
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sensitive {
  Type value() default Type.MOBILE;

  enum Type {
    MOBILE, EMAIL, ID_CARD
  }
}