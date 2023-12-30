/* (C)2023*/
package org.nectar.refined;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.nectar.refined.error.TryOr;

final class TryOrTest {

	@Test
	void attempt() {
		val t = TryOr.<Integer, NullPointerException>attempt(() -> {
			throw new NullPointerException();
		});
		System.err.println(t);
		assertInstanceOf(NullPointerException.class, t.exception());
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
