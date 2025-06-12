/* (C)2023*/
package org.toolkit4j.collection;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class ConcurrentTableBenchmark {

//	@Benchmark
//	public ConcurrentTable<String, String, Integer> testStringContainer() {
//		ConcurrentTable<String, String, Integer> testTable = new ConcurrentHashTable<>();
//		final int maxLength = 1000000;
//		IntStream.range(0, maxLength)
//				.parallel()
//				.forEach(i -> testTable.put(Integer.toHexString(i), Integer.toString(i), i));
//		return testTable;
//	}
}
