package org.toolkit4j.data.model.envelope;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;

/**
 * Generic response wrapper for API and service boundaries.
 *
 * @param <C> response code type
 * @param <T> response payload type
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Result<C, T> implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private C code;
  private String message;
  private T data;

  @Contract("_, _, _ -> new")
  public static <C, T> @NotNull Result<C, T> of(
    @Nullable C code,
    @Nullable String message,
    @Nullable T data
  ) {
    return new Result<>(code, message, data);
  }

  @Contract("_, _ -> new")
  public static <C, T> @NotNull Result<C, T> of(
    @Nullable C code,
    @Nullable String message
  ) {
    return new Result<>(code, message, null);
  }

  @Contract("_ -> new")
  public static <C, T> @NotNull Result<C, T> ofData(@Nullable T data) {
    return new Result<>(null, null, data);
  }

  @Contract("_ -> new")
  public static <C, T> @NotNull Result<C, T> ofCode(@Nullable C code) {
    return new Result<>(code, null, null);
  }

  @Contract("_ -> new")
  public static <C, T> @NotNull Result<C, T> ofMessage(@Nullable String message) {
    return new Result<>(null, message, null);
  }

  @Contract("-> new")
  public @NotNull Result<C, T> withoutData() {
    return new Result<>(this.code, this.message, null);
  }

  @Contract("_ -> new")
  public <R> @NotNull Result<C, R> withData(@Nullable R newData) {
    return new Result<>(this.code, this.message, newData);
  }

  @Contract("_ -> new")
  public <NC> @NotNull Result<NC, T> withCode(@Nullable NC newCode) {
    return new Result<>(newCode, this.message, this.data);
  }

  @Contract("_ -> new")
  public @NotNull Result<C, T> withMessage(@Nullable String newMessage) {
    return new Result<>(this.code, newMessage, this.data);
  }
}
