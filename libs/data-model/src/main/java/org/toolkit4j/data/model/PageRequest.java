package org.toolkit4j.data.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通用分页查询参数模型。
 * <p>
 * 用于接收前端分页查询的请求参数，包含：
 * <ul>
 *   <li>页码（page）</li>
 *   <li>每页大小（size）</li>
 * </ul>
 *
 * <p>
 * 特殊说明：<br>
 * {@link #getPage()} 方法返回的是数据库 Offset（偏移量），而非原始页码。
 * 即：<code>offset = size * (page - 1)</code>
 * <br>
 * 但该方法不会修改 {@code page} 字段本身，用于保持请求参数的原始语义。
 */
@Data
@SuppressWarnings("unused")
public class PageRequest {

  /**
   * 页码（从 1 开始）。
   * <p>
   * 默认为 0，则在分页计算时视为第 1 页。
   */
  private Integer page = 0; // 默认第 0 页

  /**
   * 每页数据量，默认为 10。
   */
  private Integer size = 10; // 默认每页 10 条

  /**
   * 获取分页查询的偏移量（offset）。
   * <p>
   * 内部计算规则：
   * <ul>
   *   <li>当前端传入 page >= 1 时：返回 size * (page - 1)</li>
   *   <li>page 小于 1 时：返回 0（默认从第一页开始）</li>
   * </ul>
   *
   * <p>
   * 注意：该方法不会修改原始的 {@link #page}，仅用于数据库层计算 offset。
   *
   * @return 偏移量（offset）
   */
  public Integer getPage() {
    if (page >= 1) {
      // 返回 offset，但不修改 page 字段本身
      return size * (page - 1);
    }
    return 0;
  }
}