package org.nova.cli.command

import picocli.CommandLine
import java.io.File
import java.math.BigInteger
import java.nio.file.Files
import java.security.MessageDigest
import java.util.concurrent.Callable

@CommandLine.Command(name = "checksum", mixinStandardHelpOptions = true, version = ["checksum 4.0"],
    description = ["Prints the checksum (SHA-256 by default) of a file to STDOUT."],
    subcommands = [BuildCommand::class]
)
class RootCommand :Callable<Int>{
    override fun call(): Int {
        return 0
    }
}