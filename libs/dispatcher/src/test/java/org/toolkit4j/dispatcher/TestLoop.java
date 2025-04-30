package org.toolkit4j.dispatcher;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestLoop {

    @SneakyThrows
    @Test
    void testLoop() {
        val loop =new Loop(() -> log.info("loop"));

        TimeUnit.SECONDS.sleep(10);

        loop.stop();
    }
}
