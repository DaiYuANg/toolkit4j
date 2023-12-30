/* (C)2023*/
package org.nectar.refined.container;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class StringContainerTest {

	private final String test = "test";

	private final StringContainer sc = spy(StringContainer.of(test));

	@Test
	void ifPresent() {
		sc.ifPresent(value -> System.out.println("ifPresent function executed!"));
		verify(sc, times(1)).ifPresent(any());
	}

	@Test
	void orElse() {
		assertEquals(sc.orElse("test1"), "test");
	}

	@Test
	void isEmpty() {
		assertFalse(sc.isEmpty());
	}

	@Test
	void equal() {
		assertTrue(sc.eq(test));
	}

	@Test
	void of() {
		assertNotNull(sc);
	}

	@Test
	void toArray() {
		System.err.println(Arrays.toString(sc.toArray()));
	}
}
