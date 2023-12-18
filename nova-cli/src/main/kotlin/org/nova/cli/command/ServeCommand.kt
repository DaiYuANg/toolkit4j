package org.nova.cli.command

import picocli.CommandLine.Command
import java.util.concurrent.Callable

@Command(name = "server", aliases = ["s"], mixinStandardHelpOptions = true)
class ServeCommand :Callable<Int>{
    override fun call(): Int {

        return 0;
    }
}