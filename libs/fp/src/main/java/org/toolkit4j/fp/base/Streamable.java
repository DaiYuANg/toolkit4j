/* (C)2023*/
package org.toolkit4j.fp.base;

import java.util.stream.Stream;

public interface Streamable<T> {
	Stream<T> stream();
}
