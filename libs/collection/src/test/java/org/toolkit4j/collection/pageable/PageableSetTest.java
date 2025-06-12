package org.toolkit4j.collection.pageable;

import lombok.val;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;

public class PageableSetTest {

    private final PageableList<String> pageable = spy(new PageableList<>(List.of()));

    private final Faker faker = new Faker();

    private int totalFakeData;

    @BeforeEach
    void setup() {
        val c = faker.name();
        val fakeData = IntStream.range(0, 10000)
                .mapToObj(i -> c.fullName())
                .collect(Collectors.toSet());
        totalFakeData = fakeData.size();
//        pageable.addAll(fakeData);
    }

    @Test
    void testPage() {
        val pageSize = 50;
        val paged = pageable.page(2, pageSize);
        assertEquals(paged.size(), pageSize);
    }

    @Test
    void testZeroPageNo() {
        assertThrows(IllegalArgumentException.class, () -> pageable.page(0, 10));
    }
}
