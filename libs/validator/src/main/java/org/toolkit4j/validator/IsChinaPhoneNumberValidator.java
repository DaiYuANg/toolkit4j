package org.toolkit4j.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.toolkit4j.annotation.IsChinaPhoneNumber;
import org.toolkit4j.util.PhoneNumberValidator;


public class IsChinaPhoneNumberValidator implements ConstraintValidator<IsChinaPhoneNumber, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return PhoneNumberValidator.isValid(value);
  }
}
