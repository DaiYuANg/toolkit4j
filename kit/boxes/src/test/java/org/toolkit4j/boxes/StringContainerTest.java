package org.toolkit4j.boxes;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.toolkit4j.boxes.container.StringContainer;

public class StringContainerTest {

    @Test
    void testCount() {
        val a = StringContainer.of("test");
        System.err.println(a.get().length());
    }
}
