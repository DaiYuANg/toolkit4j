package org.toolkit4j.id;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AtomicSequenceIdUtil {

  private final AtomicSequenceIdGenerator generator = new AtomicSequenceIdGenerator();

  public Long next() {
    return generator.nextId();
  }
}
