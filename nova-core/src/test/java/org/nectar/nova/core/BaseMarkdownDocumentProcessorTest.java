/* (C)2023*/
package org.nectar.nova.core;

import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@TestWithResources
@Slf4j
class BaseMarkdownDocumentProcessorTest extends GuiceJunit {

	@Test
	void processor() {
		IntStream.range(0, 100)
				.forEach(i -> injector.getInstance(Executor.class).execute(() -> log.info("test")));
	}
}
