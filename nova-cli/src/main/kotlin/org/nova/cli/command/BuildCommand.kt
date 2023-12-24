package org.nova.cli.command

import org.nova.core.*
import picocli.CommandLine
import java.io.File
import java.nio.file.Path
import java.util.concurrent.Callable

@CommandLine.Command(
    name = "build", mixinStandardHelpOptions = true, version = ["checksum 4.0"],
    description = ["Build Static Site"]
)
class BuildCommand : Callable<Int> {

    private val source: Path = Path.of("./docs")

    override fun call(): Int {
        val novaBuildLayout = NovaBuildLayout(output = Path.of(""), sourcePath = source)
        Nova(
            novaBuildLayout, Config(
                listOf(File("")),
                listOf(MetaInfo(name = "", content = ""))
            )
        ).build()
        return 0
    }
}