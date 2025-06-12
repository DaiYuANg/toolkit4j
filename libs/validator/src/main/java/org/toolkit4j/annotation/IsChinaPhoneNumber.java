package org.toolkit4j.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.toolkit4j.validator.IsChinaPhoneNumberValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsChinaPhoneNumberValidator.class)
public @interface IsChinaPhoneNumber {
  String message() default "${is.not.phone.number}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
