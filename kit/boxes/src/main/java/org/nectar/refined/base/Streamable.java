/* (C)2023*/
package org.nectar.refined.base;

import java.util.stream.Stream;

public interface Streamable<T> {
	Stream<T> stream();
}
