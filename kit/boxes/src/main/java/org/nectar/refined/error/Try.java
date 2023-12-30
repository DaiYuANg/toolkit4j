/* (C)2023*/
package org.nectar.refined.error;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter(AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
class Try<T extends Throwable> {

	private final T exception;

	@SneakyThrows
	public void justThrow() {
		if (Objects.isNull(exception)) return;
		throw exception;
	}
}
