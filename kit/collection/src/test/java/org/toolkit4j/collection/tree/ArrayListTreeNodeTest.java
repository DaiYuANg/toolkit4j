/* (C)2023*/
package org.toolkit4j.collection.tree;

import static org.mockito.Mockito.spy;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArrayListTreeNodeTest {

	//	private final ArrayListTreeNode<String> root =
	//			ArrayListTreeNode.<String>builder().data("test").build();

	private final Faker faker = spy(new Faker());

	@BeforeEach
	void setUp() {
		//		val a = ArrayListTreeNode.<String>builder().build();
		//		ArrayListTreeNode.<String>builder().data("test").build();
		//		List<TreeNode<String>> childNodes = IntStream.range(0, 5)
		//				.mapToObj(i -> ArrayListTreeNode.<String>builder()
		//						.data(faker.name().fullName())
		//						.build())
		//				.collect(Collectors.toList());
		//		root.addChildren(childNodes);
		//		new ListTreeNode<>("dads", new ListTreeNode<>(), new ArrayList<>());
		//		new ReentrantQueue<String, ArrayBlockingQueue<String>>(new ArrayBlockingQueue<>(10));
	}

	@Test
	void addChild() {}

	@Test
	void removeChild() {}

	@Test
	void walk() {}

	@Test
	void parallelWalk() {}

	@Test
	void iterator() {}

	@Test
	void stream() {}

	@Test
	void parallelStream() {}

	@Test
	void getData() {}

	@Test
	void getParent() {}

	@Test
	void testToString() {}

	@Test
	void setParent() {}

	@Test
	void isRoot() {}

	@Test
	void isLeaf() {}

	@Test
	void getChildren() {}
}
