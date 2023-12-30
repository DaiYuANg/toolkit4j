/* (C)2023*/
package org.nectar.collection;

import static org.mockito.Mockito.spy;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nectar.collection.table.ConcurrentHashTable;
import org.nectar.collection.table.ConcurrentTable;

class ConcurrentHashTableTest {

	private final ConcurrentTable<String, String, Integer> testTable = spy(new ConcurrentHashTable<>());

	private final int maxLength = 1000000;

	@BeforeEach
	void setup() {
		IntStream.range(0, maxLength)
				.parallel()
				.forEach(i -> testTable.put(Integer.toHexString(i), Integer.toString(i), i));
	}

	@Test
	void testPut() {
		Assertions.assertEquals(testTable.size(), maxLength);
	}

	@Test
	void contains() {
		int randomInRange = ThreadLocalRandom.current().nextInt(0, maxLength);
		val isContains = testTable.contains(Integer.toHexString(randomInRange), Integer.toString(randomInRange));
		Assertions.assertTrue(isContains);
	}
}
