/* (C)2023*/
package org.toolkit4j.boxes.error;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {
	void accept(T t) throws E;
}
