package org.toolkit4j.distributed;

import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class DistributedNodeUtil {

  /** 默认节点 ID */
  public final long DEFAULT_NODE_ID = 1L;

  /** 节点 ID 最小值 */
  public final long NODE_ID_MIN = 1L;

  /** 节点 ID 最大值 */
  public final long NODE_ID_MAX = 1023L;

  /**
   * 获取节点 ID。
   * <p>
   * 优先级：
   * <ol>
   *   <li>未配置时，根据主机名生成节点 ID（hash 后取模）</li>
   *   <li>生成失败时，使用默认节点 ID</li>
   * </ol>
   *
   * @return 节点 ID
   */
  public Long getNodeId() {
    return Try.of(SystemUtils::getHostName)
      .map(DistributedNodeUtil::nodeIdFromHostname)
      .getOrElse(DEFAULT_NODE_ID);
  }

  /**
   * 根据主机名生成节点 ID。
   * <p>
   * 算法：对主机名 hashCode 取绝对值，然后取模 (0~1023)。
   * 保证分布式节点在合理范围内。
   *
   * @param hostname 主机名
   * @return 节点 ID
   */
  private long nodeIdFromHostname(@NotNull String hostname) {
    var id = Math.abs(hostname.hashCode()) % (NODE_ID_MAX + 1); // 0~1023
    if (id < NODE_ID_MIN) {
      id = NODE_ID_MIN;
      return id;
    }
    return id;
  }
}