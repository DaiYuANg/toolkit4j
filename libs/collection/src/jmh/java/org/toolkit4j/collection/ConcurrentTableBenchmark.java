/* (C)2023*/
package org.toolkit4j.collection;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.toolkit4j.collection.table.ConcurrentHashTable;
import org.toolkit4j.collection.table.ConcurrentTable;
import org.openjdk.jmh.annotations.*;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class ConcurrentTableBenchmark {

	@Benchmark
	public ConcurrentTable<String, String, Integer> testStringContainer() {
		ConcurrentTable<String, String, Integer> testTable = new ConcurrentHashTable<>();
		final int maxLength = 1000000;
		IntStream.range(0, maxLength)
				.parallel()
				.forEach(i -> testTable.put(Integer.toHexString(i), Integer.toString(i), i));
		return testTable;
	}
}
