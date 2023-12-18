/* (C)2023*/
package org.nectar.nova.core;

import static org.junit.jupiter.api.Assertions.*;

import com.google.inject.testing.fieldbinder.Bind;
import org.junit.jupiter.api.Test;
import org.nectar.nova.core.html.HtmlProcessor;

class NovaTest {

	@Bind
	HtmlProcessor htmlProcessor;

	@Test
	void parser() {
		System.err.println(htmlProcessor);
		Nova.builder().build().parser();
	}
}
