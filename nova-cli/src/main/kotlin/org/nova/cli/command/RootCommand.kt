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
    @CommandLine.Parameters(index = "0", description = ["The file whose checksum to calculate."])
    lateinit var file: File

    @CommandLine.Option(names = ["-a", "--algorithm"], description = ["MD5, SHA-1, SHA-256, ..."])
    var algorithm = "SHA-256"
    override fun call(): Int {
        val fileContents = Files.readAllBytes(file.toPath())
        val digest = MessageDigest.getInstance(algorithm).digest(fileContents)
        println(("%0" + digest.size * 2 + "x").format(BigInteger(1, digest)))
        return 0
    }
}