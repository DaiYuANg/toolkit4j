package org.toolkit4j.integration.hibernate.snowflake.id;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.agrona.concurrent.SnowflakeIdGenerator;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

import static java.util.Optional.ofNullable;
import static org.toolkit4j.integration.hibernate.snowflake.id.HibernateConfigureKey.SNOW_FLAKE_HIBERNATE_PROPERTIES_KEY;

@Slf4j
public class HibernateSnowflakeIdGenerator implements IdentifierGenerator, Configurable {

  /** Snowflake ID 生成器实例 */
  private SnowflakeIdGenerator generator;

  /**
   * Hibernate 配置方法，在 SessionFactory 初始化时调用。
   * <p>
   * 作用：根据 Hibernate 配置或主机名生成节点 ID，并初始化 SnowflakeIdGenerator。
   */
  @Override
  public void configure(GeneratorCreationContext creationContext, Properties parameters) throws MappingException {
    val nodeId = getNodeId(parameters);
    log.atDebug().log("Current Node Id:{}", nodeId);
    // 初始化 Snowflake ID 生成器
    this.generator = new SnowflakeIdGenerator(nodeId);

    log.atDebug().log("Snowflake ID Generator initialized with node ID: {}", nodeId);
  }

  /**
   * 获取节点 ID。
   * <p>
   * 优先级：
   * <ol>
   *   <li>从 Hibernate 配置参数读取 {@code SNOW_FLAKE_HIBERNATE_PROPERTIES_KEY}</li>
   *   <li>未配置时，根据主机名生成节点 ID（hash 后取模）</li>
   *   <li>生成失败时，使用默认节点 ID</li>
   * </ol>
   *
   * @param params Hibernate 配置参数
   * @return 节点 ID
   */
  private Long getNodeId(@NotNull Properties params) {
    return ofNullable(params.getProperty(SNOW_FLAKE_HIBERNATE_PROPERTIES_KEY))
      .map(this::parseNodeId)
      .orElseGet(
        DistributedNodeUtil::getNodeId
      );
  }

  /**
   * 解析节点 ID 字符串。
   *
   * @param nodeIdString 节点 ID 字符串
   * @return 节点 ID
   * @throws IllegalArgumentException 当节点 ID 不是合法数字时
   */
  private Long parseNodeId(String nodeIdString) {
    return Try.of(() -> Long.parseLong(nodeIdString))
      .getOrElseThrow(
        (e) -> {
          throw new IllegalArgumentException(
            "Invalid node ID for SnowflakeIdGenerator: " + nodeIdString, e);
        });
  }

  /**
   * Hibernate 实体主键生成方法。
   *
   * @param session Hibernate 会话
   * @param object  实体对象
   * @return 生成的唯一主键
   * @throws IllegalStateException 如果 SnowflakeIdGenerator 未初始化
   */
  @Override
  public Object generate(SharedSessionContractImplementor session, Object object) {
    return ofNullable(generator)
      .orElseThrow(
        () ->
          new IllegalStateException(
            "HibernateSnowflakeIdGenerator is not properly configured"))
      .nextId();
  }
}