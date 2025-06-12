/* (C)2023*/
package org.toolkit4j.fp.exception;

import lombok.experimental.StandardException;

import java.io.Serial;

@StandardException
public class EmptyCollection extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;
}
