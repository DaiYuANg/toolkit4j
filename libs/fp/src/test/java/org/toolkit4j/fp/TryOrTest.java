/* (C)2023*/
package org.toolkit4j.fp;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.toolkit4j.fp.error.TryOr;

final class TryOrTest {

	@Test
	void attempt() {
		val t = TryOr.<Integer, NullPointerException>attempt(() -> {
			throw new NullPointerException();
		});
		System.err.println(t);
//		assertInstanceOf(NullPointerException.class, t.exception());
	}

	@Test
	void ifError() {}

	@Test
	void orElse() {}

	@Test
	void getResult() {}

	@Test
	void getE() {}

	@Test
	void setE() {}
}
