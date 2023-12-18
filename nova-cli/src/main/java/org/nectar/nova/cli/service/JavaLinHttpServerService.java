package org.nectar.nova.cli.service;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class JavaLinHttpServerService implements HttpServerService {

    @Override
    public void start(int port) {
        val server = Javalin.create();
        server.start(port);
    }
}
