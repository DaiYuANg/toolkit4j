/* (C)2023*/
package org.nova.core;

import com.google.inject.testing.fieldbinder.Bind;
import org.junit.jupiter.api.Test;
import org.nova.core.html.HtmlProcessor;

class NovaTest {

	@Bind
	HtmlProcessor htmlProcessor;

	@Test
	void parser() {
		System.err.println(htmlProcessor);
		Nova.builder().build().parser();
	}
}
