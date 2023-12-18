package org.nova.cli.command

import picocli.CommandLine
import java.util.concurrent.Callable

@CommandLine.Command(name = "build", mixinStandardHelpOptions = true, version = ["checksum 4.0"],
    description = ["Build Static Site"])
class BuildCommand :Callable<Int>{
    override fun call(): Int {

        return 0
    }
}