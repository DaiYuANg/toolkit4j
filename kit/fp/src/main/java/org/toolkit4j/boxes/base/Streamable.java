/* (C)2023*/
package org.toolkit4j.boxes.base;

import java.util.stream.Stream;

public interface Streamable<T> {
	Stream<T> stream();
}
