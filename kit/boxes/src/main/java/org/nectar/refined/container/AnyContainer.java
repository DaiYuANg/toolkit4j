/* (C)2023*/
package org.nectar.refined.container;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class AnyContainer<T extends Containerizeable> extends BasicContainer<T, AnyContainer<T>> {

	protected AnyContainer(T value) {
		super(value);
	}

	@Override
	public AnyContainer<T> filter(Predicate<T> predicate) {
		return isEmpty() ? this : predicate.test(value) ? this : empty();
	}

	@Override
	public <U extends Container<T, ?>> AnyContainer<T> map(Function<? super T, U> mapper) {
		if (isEmpty()) {
			return empty();
		} else {
			@SuppressWarnings("unchecked")
			val result = (AnyContainer<T>) mapper.apply(value);
			return result;
		}
	}

	@Override
	public <U> AnyContainer<T> flatMap(Function<? super T, ? extends Container<T, ? extends U>> mapper) {
		if (isEmpty()) {
			return empty();
		} else {
			@SuppressWarnings("unchecked")
			val r = (AnyContainer<T>) mapper.apply(value);
			return r;
		}
	}

	@SneakyThrows
	@Override
	public T orElseThrow() {
		throw nonNullable().defaultThrow();
	}

	@Override
	public T get() {
		return value;
	}

	public boolean isValid() {
		return nonNullable().isValid();
	}

	@Override
	public AnyContainer<T> or(Supplier<AnyContainer<T>> supplier) {
		return isEmpty() ? supplier.get() : this;
	}

	@NotNull @Contract("_ -> new")
	public static <T extends Containerizeable> AnyContainer<T> of(T value) {
		return new AnyContainer<>(value);
	}

	@NotNull public static <T extends Containerizeable> AnyContainer<T> empty() {
		return new AnyContainer<>(null);
	}
}
