package org.toolkit4j.hibernate.snowflake.id;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.agrona.concurrent.SnowflakeIdGenerator;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Properties;

import static java.util.Optional.ofNullable;
import static org.toolkit4j.hibernate.snowflake.id.HibernateConfigureKey.SNOW_FLAKE_HIBERNATE_PROPERTIES_KEY;

@Slf4j
public class HibernateSnowflakeIdGenerator implements IdentifierGenerator, Configurable {

  private SnowflakeIdGenerator generator;

  @Override
  public void configure(GeneratorCreationContext creationContext, Properties parameters) throws MappingException {
    val nodeId = resolveNodeId(parameters);
    validateNodeIdForDefaultAgronaLayout(nodeId);
    log.atDebug().log("Current Node Id:{}", nodeId);
    this.generator = new SnowflakeIdGenerator(nodeId);
    log.atDebug().log("Snowflake ID Generator initialized with node ID: {}", nodeId);
  }

  /**
   * Agrona {@link SnowflakeIdGenerator#SnowflakeIdGenerator(long)} 使用默认 10 位 node 位，合法范围为 {@code [0, 1023]}。
   */
  private static void validateNodeIdForDefaultAgronaLayout(long nodeId) {
    long maxNodeId = (1L << SnowflakeIdGenerator.NODE_ID_BITS_DEFAULT) - 1;
    if (nodeId < 0 || nodeId > maxNodeId) {
      throw new MappingException(
        "snowflake node-id must be between 0 and " + maxNodeId + " (inclusive) for default Agrona layout, got: " + nodeId
      );
    }
  }

  private long resolveNodeId(@NotNull Properties params) {
    return ofNullable(params.getProperty(SNOW_FLAKE_HIBERNATE_PROPERTIES_KEY))
      .map(HibernateSnowflakeIdGenerator::parseConfiguredNodeId)
      .orElseGet(DistributedNodeUtil::getNodeId);
  }

  private static long parseConfiguredNodeId(String nodeIdString) {
    try {
      return Long.parseLong(nodeIdString.trim());
    } catch (NumberFormatException e) {
      throw new MappingException("Invalid node ID for SnowflakeIdGenerator: " + nodeIdString, e);
    }
  }

  @Override
  public Object generate(SharedSessionContractImplementor session, Object object) {
    return Objects.requireNonNull(generator, "HibernateSnowflakeIdGenerator is not properly configured").nextId();
  }
}
