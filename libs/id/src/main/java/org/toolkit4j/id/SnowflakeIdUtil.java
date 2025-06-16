package org.toolkit4j.id;

import lombok.experimental.UtilityClass;
import org.agrona.concurrent.SnowflakeIdGenerator;

/**
 * Re-wrapper of agrona.SnowflakeIdGenerator for simple usage
 */
@UtilityClass
public class SnowflakeIdUtil {

  private final SnowflakeIdGenerator internalGenerator = new SnowflakeIdGenerator(1L);

  public Long nextId() {
    return internalGenerator.nextId();
  }

  public String nextIdString() {
    return String.valueOf(internalGenerator.nextId());
  }
}