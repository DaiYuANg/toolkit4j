package org.toolkit4j.hibernate.snowflake.id;

import static org.toolkit4j.hibernate.snowflake.id.HibernateConfigureKey.SNOW_FLAKE_HIBERNATE_PROPERTIES_KEY;

import java.util.Objects;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.agrona.concurrent.SnowflakeIdGenerator;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class HibernateSnowflakeIdGenerator implements IdentifierGenerator, Configurable {

  private SnowflakeIdGenerator generator;

  @Override
  public void configure(GeneratorCreationContext creationContext, Properties parameters)
      throws MappingException {
    log.debug("Configuring Hibernate Snowflake ID generator");
    val nodeId = resolveNodeId(parameters);
    validateNodeIdForDefaultAgronaLayout(nodeId);
    this.generator = new SnowflakeIdGenerator(nodeId);
    log.debug("Initialized Hibernate Snowflake ID generator with nodeId={}", nodeId);
  }

  /**
   * Agrona {@link SnowflakeIdGenerator#SnowflakeIdGenerator(long)} 使用默认 10 位 node 位，合法范围为 {@code
   * [0, 1023]}。
   */
  private void validateNodeIdForDefaultAgronaLayout(long nodeId) {
    long maxNodeId = (1L << SnowflakeIdGenerator.NODE_ID_BITS_DEFAULT) - 1;
    if (nodeId < 0 || nodeId > maxNodeId) {
      throw new MappingException(
          "snowflake node-id must be between 0 and "
              + maxNodeId
              + " (inclusive) for default Agrona layout, got: "
              + nodeId);
    }
  }

  private long resolveNodeId(@NotNull Properties params) {
    val configuredNodeId = params.getProperty(SNOW_FLAKE_HIBERNATE_PROPERTIES_KEY);
    if (configuredNodeId != null) {
      val parsedNodeId = parseConfiguredNodeId(configuredNodeId);
      log.debug(
          "Using explicitly configured snowflake nodeId={} from property {}",
          parsedNodeId,
          SNOW_FLAKE_HIBERNATE_PROPERTIES_KEY);
      return parsedNodeId;
    }

    val resolvedNodeId = DistributedNodeUtil.getNodeId();
    log.debug(
        "Using derived snowflake nodeId={} because property {} was not set",
        resolvedNodeId,
        SNOW_FLAKE_HIBERNATE_PROPERTIES_KEY);
    return resolvedNodeId;
  }

  private long parseConfiguredNodeId(@NotNull String nodeIdString) {
    try {
      return Long.parseLong(nodeIdString.trim());
    } catch (NumberFormatException e) {
      throw new MappingException("Invalid node ID for SnowflakeIdGenerator: " + nodeIdString, e);
    }
  }

  @Override
  public Object generate(SharedSessionContractImplementor session, Object object) {
    return Objects.requireNonNull(
            generator, "HibernateSnowflakeIdGenerator is not properly configured")
        .nextId();
  }
}
