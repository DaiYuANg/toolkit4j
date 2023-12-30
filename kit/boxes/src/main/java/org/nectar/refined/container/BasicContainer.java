/* (C)2023*/
package org.nectar.refined.container;

import static java.util.Optional.ofNullable;
import static java.util.stream.Stream.of;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@EqualsAndHashCode
public abstract class BasicContainer<T, I extends BasicContainer<T, I>> implements Container<T, I> {

	@Nullable protected final T value;

	protected BasicContainer(@Nullable T value) {
		this.value = value;
	}

	@Override
	public Stream<T> stream() {
		return of(value);
	}

	@Override
	public Optional<T> toOptional() {
		return ofNullable(value);
	}

	@SuppressWarnings("IsNull")
	@Override
	public boolean isEmpty() {
		return Objects.isNull(value);
	}

	@Override
	public CompletableFuture<T> toCompletableFuture() {
		return CompletableFuture.supplyAsync(() -> value);
	}

	@Override
	@SneakyThrows
	public <ErrorException extends Exception> T orElseThrow(Class<ErrorException> errorExceptionClass) {
		if (isEmpty()) throw errorExceptionClass.getDeclaredConstructor().newInstance();
		return value;
	}

	@Override
	public <ErrorException extends Exception> T orElseThrow(Supplier<? extends ErrorException> exceptionSupplier)
			throws ErrorException {
		if (isEmpty()) throw exceptionSupplier.get();
		return value;
	}

	@Override
	public <ErrorException extends Exception> T orElseThrow(ErrorException exception) throws ErrorException {
		if (isEmpty()) throw exception;
		return value;
	}

	@Override
	public T orElse(T other) {
		return Objects.requireNonNullElse(value, other);
	}

	@Override
	public T orElseGet(Supplier<T> supplier) {
		return Objects.requireNonNullElseGet(value, supplier);
	}

	@Override
	public void ifPresentOrElse(Consumer<T> action, Runnable emptyAction) {
		Runnable runnableAction = () -> action.accept(value);
		(isEmpty() ? runnableAction : emptyAction).run();
	}

	@Override
	public T orElse(@NotNull Supplier<T> supplier) {
		return Objects.requireNonNullElse(value, supplier.get());
	}

	@Override
	public void ifPresent(Consumer<T> action) {
		if (!isEmpty()) action.accept(value);
	}

	@Override
	public boolean eq(Object o) {
		return Objects.equals(value, o);
	}

	@Override
	@NotNull public T nonNullable() {
		return Objects.requireNonNull(value);
	}

	public boolean isValid() {
		return true;
	}
}
