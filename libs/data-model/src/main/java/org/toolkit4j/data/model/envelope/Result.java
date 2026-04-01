package org.toolkit4j.data.model.envelope;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Generic response wrapper for API and service boundaries.
 *
 * <p>This type is intentionally kept generic and lightweight. It models only the envelope structure
 * itself and does not impose any success/failure semantics on {@code code}. If success/failure
 * semantics are needed, they should be defined by the code type itself or by helper utilities
 * outside this class.
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

  @Serial private static final long serialVersionUID = 1L;

  private C code;
  private String message;
  private T data;

  @Contract("_, _, _ -> new")
  public static <C, T> @NotNull Result<C, T> of(
      @Nullable C code, @Nullable String message, @Nullable T data) {
    return new Result<>(code, message, data);
  }

  @Contract("_, _ -> new")
  public static <C, T> @NotNull Result<C, T> of(@Nullable C code, @Nullable String message) {
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
  public @NotNull Result<C, T> copy() {
    return new Result<>(this.code, this.message, this.data);
  }

  @Contract("-> new")
  public @NotNull Result<C, T> withoutData() {
    return new Result<>(this.code, this.message, null);
  }

  @Contract("-> new")
  public @NotNull Result<C, T> withoutMessage() {
    return new Result<>(this.code, null, this.data);
  }

  @Contract("-> new")
  public @NotNull Result<C, T> withoutCode() {
    return new Result<>(null, this.message, this.data);
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

  @Contract(pure = true)
  public boolean hasCode() {
    return this.code != null;
  }

  @Contract(pure = true)
  public boolean hasMessage() {
    return this.message != null && !this.message.isBlank();
  }

  @Contract(pure = true)
  public boolean hasData() {
    return this.data != null;
  }

  @Contract(pure = true)
  public @Nullable T getDataOrNull() {
    return this.data;
  }

  @Contract(pure = true)
  public T getDataOrDefault(@Nullable T defaultValue) {
    return this.data != null ? this.data : defaultValue;
  }

  @Contract(pure = true)
  public T getDataOrElse(@NotNull Supplier<? extends T> supplier) {
    return this.data != null ? this.data : supplier.get();
  }

  @Contract(pure = true)
  public @NotNull Optional<T> dataOptional() {
    return Optional.ofNullable(this.data);
  }

  @Contract("_ -> new")
  public <R> @NotNull Result<C, R> mapData(@NotNull Function<? super T, ? extends R> mapper) {
    return new Result<>(
        this.code, this.message, this.data == null ? null : mapper.apply(this.data));
  }

  @Contract("_ -> new")
  public <NC> @NotNull Result<NC, T> mapCode(@NotNull Function<? super C, ? extends NC> mapper) {
    return new Result<>(
        this.code == null ? null : mapper.apply(this.code), this.message, this.data);
  }

  @Contract("_ -> new")
  public @NotNull Result<C, T> mapMessage(
      @NotNull Function<? super String, ? extends String> mapper) {
    return new Result<>(
        this.code, this.message == null ? null : mapper.apply(this.message), this.data);
  }
}
