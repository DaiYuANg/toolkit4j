/* (C)2023*/
package org.nectar.refined.container;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.NonNull;

public class NumberContainer<T extends Number> extends BasicContainer<T, NumberContainer<T>> {
	protected NumberContainer(T value) {
		super(value);
	}

	@Override
	public NumberContainer<T> filter(@NonNull Predicate<T> predicate) {
		return null;
	}

	@Override
	public <U extends Container<T, ?>> NumberContainer<T> map(@NonNull Function<? super T, U> mapper) {
		return null;
	}

	@Override
	public T orElseThrow() {
		return null;
	}

	@Override
	public T get() {
		return null;
	}

	@Override
	public NumberContainer<T> or(@NonNull Supplier<NumberContainer<T>> supplier) {
		return null;
	}
}
