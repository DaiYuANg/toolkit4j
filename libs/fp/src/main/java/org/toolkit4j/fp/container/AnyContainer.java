/* (C)2023*/
package org.toolkit4j.fp.container;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings({"unused","unchecked"})
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
      U result = mapper.apply(value);
      // Assuming U is of type AnyContainer<T>
      return (AnyContainer<T>) result;
    }
  }

  @Override
  public <U> AnyContainer<T> flatMap(Function<? super T, ? extends Container<T, ? extends U>> mapper) {
    if (isEmpty()) {
      return empty();
    } else {
      Container<T, ? extends U> result = mapper.apply(value);
      // Assuming result is of type AnyContainer<T>
      return (AnyContainer<T>) result;
    }
  }

  @Override
  public T orElseThrow() {
    if (isEmpty()) {
      throw new NoSuchElementException("No value present");
    }
    return value;
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

  @Contract("_ -> new")
  public static <T extends Containerizeable> @NotNull AnyContainer<T> of(T value) {
    return new AnyContainer<>(value);
  }

  @Contract(" -> new")
  public static <T extends Containerizeable> @NotNull AnyContainer<T> empty() {
    return new AnyContainer<>(null);
  }
}
