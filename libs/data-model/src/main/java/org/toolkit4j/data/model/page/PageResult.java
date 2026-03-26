package org.toolkit4j.data.model.page;

import io.soabase.recordbuilder.core.RecordBuilder;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static java.util.Optional.ofNullable;

/**
 * 通用分页结果模型。
 * <p>
 * 用于统一后端分页数据的返回格式，适配 Repository 层查询结果与 Controller 层 API 输出。
 * 支持任意类型的内容列表（content），并包含分页信息：当前页码、每页大小、
 * 总元素数量以及总页数。
 * <p>
 * 本结构通常与 {@code PageRequest} 配合使用。
 *
 * @param content       当前页的数据内容
 * @param page          当前页码（从 1 开始）
 * @param size          每页数据量
 * @param totalElements 总数据条数
 * @param totalPages    总页数
 * @param <T>           内容的数据类型
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class PageResult<T> {
  private static final int DEFAULT_PAGE = 1;

  private Collection<T> content;
  private Integer page;
  private Integer size;
  private Long totalElements;
  private Long totalPages;

  public boolean isEmpty() {
    return Objects.isNull(content) || content.isEmpty();
  }

  public boolean hasContent() {
    return !isEmpty();
  }

  @Contract(" -> new")
  public @NotNull PageResult<T> normalized() {
    return new PageResult<>(
      ofNullable(content).orElseGet(ArrayList::new),
      ofNullable(page).orElse(DEFAULT_PAGE),
      ofNullable(size).orElse(0),
      ofNullable(totalElements).orElse(0L),
      ofNullable(totalPages).orElse(0L)
    );
  }

  /**
   * 创建一个空的分页结果。
   * <p>
   * 用于无数据场景（例如搜索结果为空），返回空列表和零分页信息。
   *
   * @param <T> 数据类型
   * @return 一个空的 {@link PageResult}
   */
  @Contract(" -> new")
  public static <T> @NotNull PageResult<T> empty() {
    return new PageResult<>(new ArrayList<>(), DEFAULT_PAGE, 0, 0L, 0L);
  }
}
