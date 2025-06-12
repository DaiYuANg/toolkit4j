/* (C)2023*/
package org.toolkit4j.fp.container;

import static org.mockito.Mockito.spy;

import java.util.List;
import lombok.val;
import org.junit.jupiter.api.Test;

class CollectionContainerTest {

	private final CollectionContainer<Integer> cc = spy(CollectionContainer.of(List.of()));

	@Test
	void elementParallelStream() {
		val parallel = cc.elementParallelStream();
//		assertTrue(parallel.isParallel());
	}

	@Test
	void filter() {}

	@Test
	void map() {}

	@Test
	void empty() {
//		assertTrue(CollectionContainer.empty().isValid());
	}

	@Test
	void get() {
//		assertThrows(EmptyCollection.class, cc::get);
	}

	@Test
	void isEmpty() {
//		assertTrue(cc.isValid());
	}
}
