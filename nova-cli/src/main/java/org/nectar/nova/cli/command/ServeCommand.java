package org.nectar.nova.cli.command;

import io.javalin.Javalin;
import lombok.val;
import picocli.CommandLine;

@CommandLine.Command
public class ServeCommand implements Runnable {

    @Override
    public void run() {
        val lin = Javalin.create(javalinConfig -> {
        });
        lin.start();
    }
}
