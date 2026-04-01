package org.toolkit4j.data.model.page;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 通用分页查询参数模型。
 *
 * <p>用于接收前端分页查询的请求参数，包含：
 *
 * <ul>
 *   <li>页码（page）
 *   <li>每页大小（size）
 * </ul>
 *
 * <p>数据库存储层如需 offset，请使用 {@link #getOffset()}。
 */
@Data
@SuppressWarnings("unused")
public class PageRequest {

  private static final int DEFAULT_PAGE = 1;
  private static final int DEFAULT_SIZE = 10;

  /** 页码（从 1 开始）。 */
  private Integer page = DEFAULT_PAGE;

  /** 每页数据量，默认为 10。 */
  private Integer size = DEFAULT_SIZE;

  /**
   * 获取分页查询的偏移量（offset）。
   *
   * <p>内部计算规则：
   *
   * <ul>
   *   <li>当前端传入 page >= 1 时：返回 size * (page - 1)
   *   <li>page 小于 1 或为 null 时：返回 0（默认从第一页开始）
   *   <li>size 小于 1 或为 null 时：使用默认值 {@value #DEFAULT_SIZE}
   * </ul>
   *
   * @return 偏移量（offset）
   */
  public @NotNull Integer getOffset() {
    return resolveSize() * Math.max(resolvePage() - 1, 0);
  }

  private int resolvePage() {
    return page == null || page < 1 ? DEFAULT_PAGE : page;
  }

  private int resolveSize() {
    return size == null || size < 1 ? DEFAULT_SIZE : size;
  }
}
