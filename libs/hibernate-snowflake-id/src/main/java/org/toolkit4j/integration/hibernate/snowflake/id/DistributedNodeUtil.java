package org.toolkit4j.integration.hibernate.snowflake.id;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;

@UtilityClass
public class DistributedNodeUtil {

  /** 默认节点 ID */
  public final long DEFAULT_NODE_ID = 1L;

  /** 主机名派生节点 ID 时的最小值（与 Agrona 默认布局兼容，0 保留给显式配置） */
  public final long NODE_ID_MIN = 1L;

  /** 节点 ID 最大值（与 {@link org.agrona.concurrent.SnowflakeIdGenerator#NODE_ID_BITS_DEFAULT} 一致：10 bit → 0..1023） */
  public final long NODE_ID_MAX = 1023L;

  /**
   * 获取节点 ID。
   * <p>
   * 优先级：
   * <ol>
   *   <li>根据本机主机名生成节点 ID（hash 后取模）</li>
   *   <li>解析失败时使用 {@link #DEFAULT_NODE_ID}</li>
   * </ol>
   * <p>
   * 注意：不同主机可能映射到同一 ID，生产环境建议显式配置 {@code snow.flake.generator.node-id}。
   *
   * @return 节点 ID
   */
  public long getNodeId() {
    String hostname = localHostNameOrNull();
    if (hostname == null || hostname.isBlank()) {
      return DEFAULT_NODE_ID;
    }
    return nodeIdFromHostname(hostname);
  }

  /**
   * 根据主机名生成节点 ID（用于测试或自定义主机名字符串）。
   * <p>
   * 使用无符号方式处理 {@link String#hashCode()}，避免 {@link Math#abs(int)} 对 {@link Integer#MIN_VALUE} 的行为及负数取模问题。
   *
   * @param hostname 主机名
   * @return 落在 {@code [NODE_ID_MIN, NODE_ID_MAX]} 的节点 ID
   */
  public static long nodeIdFromHostname(@NotNull String hostname) {
    long mix = Integer.toUnsignedLong(hostname.hashCode());
    long id = mix % (NODE_ID_MAX + 1);
    return Math.max(id, NODE_ID_MIN);
  }

  private static String localHostNameOrNull() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      return null;
    }
  }
}
