/* (C)2023*/
package org.toolkit4j.collection.pageable;

import static org.mockito.Mockito.spy;

import java.util.LinkedList;
import java.util.stream.IntStream;
import lombok.val;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PageableListTest {

	private final PageableList<String> pageable = spy(new PageableList<>(new LinkedList<>()));

	private final Faker faker = new Faker();

	@BeforeEach
	public void setup() {
		val c = faker.camera();
		val fakeData = IntStream.range(0, 100000).mapToObj(i -> c.brand()).toList();
		pageable.addAll(fakeData);
	}

	@Test
	void testPage() {
		System.err.println(pageable.page(1, 1000));
		System.err.println(pageable.current());
	}
}
