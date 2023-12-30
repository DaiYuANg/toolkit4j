/* (C)2023*/
package org.nectar.refined.error;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ToString(callSuper = true)
@SuperBuilder(toBuilder = true, builderMethodName = "b")
public class TryOr<T, E extends Exception> extends Try<E> {

	@Nullable private final T result;

	public static <T, E extends Exception> @NotNull TryOr<T, E> attempt(Supplier<T> supplier) {
		val builder = TryOr.<T, E>b();
		try {
			T result = supplier.get();
			return builder.result(result).exception(null).build();
		} catch (Exception ex) {
			@SuppressWarnings("unchecked")
			val e = (E) ex;
			return builder.exception(e).build();
		}
	}

	public void ifError(Consumer<Exception> errorHandler) {
		if (super.getException() == null) return;
		errorHandler.accept(super.getException());
	}

	public T orElse(T elseValue) {
		return elseValue;
	}

	@SuppressWarnings("LexicographicalAnnotationListing")
	@SneakyThrows
	public <U> @NotNull TryOr<U, E> map(@NotNull Function<T, U> mapper) {
		if (super.getException() != null) throw super.getException().getCause();
		return TryOr.<U, E>b().result(mapper.apply(result)).build();
	}

	@SneakyThrows
	@SuppressWarnings({"LexicographicalAnnotationListing", "RequireNonNullWithMessageStatement"})
	public <U> TryOr<U, E> flatMap(@NonNull @NotNull Function<T, TryOr<U, E>> mapper) {
		if (super.getException() != null) throw super.getException().getCause();
		return mapper.apply(result);
	}

	@NotNull public Optional<T> resultToOptional() {
		return Optional.ofNullable(result);
	}

	@NotNull public Optional<E> exceptionToOptional() {
		return Optional.ofNullable(super.getException());
	}

	public E exception() {
		return super.getException();
	}
}
