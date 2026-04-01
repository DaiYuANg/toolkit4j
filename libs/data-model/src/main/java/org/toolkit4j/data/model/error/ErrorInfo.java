package org.toolkit4j.data.model.error;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class ErrorInfo<C> {
  private C code;
  private String message;
  private List<ErrorDetail> details = List.of();

  public ErrorInfo(C code, String message, List<ErrorDetail> details) {
    this.code = code;
    this.message = message;
    this.details = List.copyOf(Objects.requireNonNullElse(details, List.of()));
  }

  public static <C> @NotNull ErrorInfo<C> of(C code, String message) {
    return new ErrorInfo<>(code, message, List.of());
  }

  public static <C> @NotNull ErrorInfo<C> of(C code, String message, ErrorDetail... details) {
    return new ErrorInfo<>(code, message, List.of(details));
  }

  public void setDetails(List<ErrorDetail> details) {
    this.details = List.copyOf(Objects.requireNonNullElse(details, List.of()));
  }

  public boolean hasDetails() {
    return !details.isEmpty();
  }

  public int detailCount() {
    return details.size();
  }

  public @NotNull ErrorInfo<C> withDetail(@NotNull ErrorDetail detail) {
    var merged = new ArrayList<>(details);
    merged.add(Objects.requireNonNull(detail, "detail"));
    return new ErrorInfo<>(code, message, merged);
  }
}
