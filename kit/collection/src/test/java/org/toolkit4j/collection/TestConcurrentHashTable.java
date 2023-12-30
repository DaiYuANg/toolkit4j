/* (C)2023*/
package org.toolkit4j.collection;

import org.junit.jupiter.api.Test;
import org.toolkit4j.collection.table.ConcurrentHashTable;
import org.toolkit4j.collection.table.ConcurrentHashTable;

public class TestConcurrentHashTable {

	private ConcurrentHashTable<String, Integer, Integer> concurrentHashTable = new ConcurrentHashTable<>();

	@Test
	void testPut() {
		concurrentHashTable.put("test", 1, 2);
	}
}
