/* (C)2023*/
package org.nectar.refined;

import java.util.concurrent.TimeUnit;
import org.nectar.refined.container.StringContainer;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class StringContainerBenchmark {

	private static final StringContainer stringContainer = StringContainer.of("Hello, World!");
	private static final String nativeString = "Hello, World!";

	@Benchmark
	public StringContainer testStringContainer() {
		return stringContainer.reverse();
	}

	@Benchmark
	public String testNativeString() {
		return new StringBuilder(nativeString).reverse().toString();
	}
}
