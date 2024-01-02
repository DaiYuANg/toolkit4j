/* (C)2023*/
package org.toolkit4j.collection.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
class ListTreeNodeTest {

    private final ListTreeNode<String> root = new ListTreeNode<>("root");

    private final Faker faker = spy(new Faker());

    private final ListTreeNode<String> specificallyChild = new ListTreeNode<>("specificallyChild", root);

    private final int fakeDataSize = 10;

    @BeforeEach
    void setUp() {
        List<TreeNode<String>> childNodes = IntStream.range(0, fakeDataSize)
                .mapToObj(i -> {
                    val secondRoot = new ListTreeNode<>("second:" + faker.name().fullName(), root);
                    val children = IntStream
                            .range(0, fakeDataSize)
                            .mapToObj(t -> new ListTreeNode<>(faker.name().fullName(), secondRoot))
                            .toList();
                    secondRoot.addChildren(children);
                    return secondRoot;
                })
                .collect(Collectors.toList());
        root.addChildren(childNodes);
        root.addChild(specificallyChild);
    }

    @Test
    void addChild() {
        assertEquals(root.getChildren().size(), fakeDataSize);
    }

    @Test
    void removeChild() {
        log.info("Before remove:{}", root.getChildren());
        root.removeChild(specificallyChild);
        log.info("After remove:{}", root.getChildren());
        assertEquals(root.getChildren().size(), 10);
    }

    @Test
    void walk() {
        root.walk(System.err::println);
    }

    @Test
    void parallelWalk() {
    }

    @Test
    void iterator() {
    }

    @Test
    void stream() {
    }

    @Test
    void parallelStream() {
    }

    @Test
    void getData() {
    }

    @Test
    void getParent() {
    }

    @Test
    void testToString() {
    }

    @Test
    void setParent() {
    }

    @Test
    void isRoot() {
    }

    @Test
    void isLeaf() {
    }

    @Test
    void getChildren() {
    }
}
