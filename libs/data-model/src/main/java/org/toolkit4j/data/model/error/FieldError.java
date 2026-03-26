package org.toolkit4j.data.model.error;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FieldError implements ErrorDetail {
  private String field;
  private String code;
  private String message;
  private Object rejectedValue;
}
