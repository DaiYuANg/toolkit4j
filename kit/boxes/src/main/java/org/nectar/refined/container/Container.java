/* (C)2023*/
package org.nectar.refined.container;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.nectar.refined.base.Streamable;

/**
 * This interface represents a generic container that holds a value of a specified type.
 * @author DaiYuAng
 * @since 0.1
 * @param <Type>      The type of the value held by the container.
 * @param <Implement>  The implementing type of the container.
 */
public interface Container<Type, Implement extends Container<Type, Implement>> extends Streamable<Type> {

	/**
	 * Performs the given action if the value is present.
	 *
	 * @param action The action to be performed if the value is present.
	 */
	void ifPresent(Consumer<Type> action);

	/**
	 * Returns the value if present, otherwise returns the specified alternative value.
	 *
	 * @param other The alternative value to be returned if the original value is not present.
	 * @return The value if present, or the alternative value.
	 */
	Type orElse(Type other);

	/**
	 * Returns the value if present; otherwise, returns the result of invoking the supplier.
	 *
	 * @param supplier The supplier of the alternative value to be returned if the original value is not present.
	 * @return The value if present, or the result of invoking the supplier.
	 */
	Type orElseGet(Supplier<Type> supplier);

	/**
	 * Performs the given action if the value is present; otherwise, performs the empty action.
	 *
	 * @param action      The action to be performed if the value is present.
	 * @param emptyAction The action to be performed if the value is not present.
	 */
	void ifPresentOrElse(Consumer<Type> action, Runnable emptyAction);

	/**
	 * Checks if the container is empty.
	 *
	 * @return true if the container is empty, false otherwise.
	 */
	boolean isEmpty();

	/**
	 * Filters the container's value based on the provided predicate.
	 *
	 * @param predicate The predicate to test the value.
	 * @return The container after filtering.
	 */
	Implement filter(Predicate<Type> predicate);

	/**
	 * Maps the container's value to another type using the provided mapper function.
	 *
	 * @param <U>    The type to which the value is mapped.
	 * @param mapper The mapping function.
	 * @return The container with the mapped value.
	 */
	<U extends Container<Type, ?>> Implement map(Function<? super Type, U> mapper);

	/**
	 * Applies a mapping function to the value and returns the resulting container.
	 *
	 * @param <U>    The type to which the value is mapped.
	 * @param mapper The mapping function.
	 * @return The container with the mapped value.
	 * @throws UnsupportedOperationException if this method is called.
	 */
	default <U> Implement flatMap(Function<? super Type, ? extends Container<Type, ? extends U>> mapper) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the value if present; otherwise, returns the result of invoking the supplier.
	 *
	 * @param supplier The supplier of the alternative container to be returned if the original container is not present.
	 * @return The container with the value if present, or the result of invoking the supplier.
	 */
	Type orElse(Supplier<Type> supplier);

	/**
	 * Returns the value if present; otherwise, throws an exception.
	 *
	 * @return The value if present.
	 */
	Type orElseThrow();

	/**
	 * Returns the value held by the container.
	 *
	 * @return The value held by the container.
	 */
	Type get();

	/**
	 * Returns this container if it is not empty; otherwise, returns the result of invoking the supplier.
	 *
	 * @param supplier The supplier of the alternative container to be returned if this container is empty.
	 * @return This container if not empty, or the result of invoking the supplier.
	 */
	Implement or(Supplier<Implement> supplier);

	/**
	 * Returns the value if present; otherwise, throws an exception determined by the specified class.
	 *
	 * @param <ErrorException> The type of exception to be thrown.
	 * @param exceptionClass   The class of the exception to be thrown.
	 * @return The value if present.
	 * @throws ErrorException if the value is not present.
	 */
	<ErrorException extends Exception> Type orElseThrow(Class<ErrorException> exceptionClass) throws ErrorException;

	<ErrorException extends Exception> Type orElseThrow(Supplier<? extends ErrorException> exceptionSupplier)
			throws ErrorException;

	/**
	 * Returns the value if present; otherwise, throws the specified exception.
	 *
	 * @param <ErrorException> The type of exception to be thrown.
	 * @param exception        The exception to be thrown.
	 * @return The value if present.
	 * @throws ErrorException if the value is not present.
	 */
	<ErrorException extends Exception> Type orElseThrow(ErrorException exception) throws ErrorException;

	/**
	 * Returns an {@code Optional} with the value, if present, otherwise returns an empty {@code Optional}.
	 *
	 * @return An {@code Optional} containing the value if present, otherwise an empty {@code Optional}.
	 */
	Optional<Type> toOptional();

	/**
	 * Returns a {@code CompletableFuture} that is completed with the value if present; otherwise, completed with null.
	 *
	 * @return A {@code CompletableFuture} that is completed with the value if present, otherwise, completed with null.
	 */
	CompletableFuture<Type> toCompletableFuture();

	/**
	 * Checks if this container is equal to the specified object.
	 *
	 * @param o The object to compare with.
	 * @return true if the objects are equal, false otherwise.
	 */
	boolean eq(Object o);

	/**
	 * Returns the non-nullable value. Throws an exception if the value is null.
	 *
	 * @return The non-nullable value.
	 */
	Type nonNullable();
}
