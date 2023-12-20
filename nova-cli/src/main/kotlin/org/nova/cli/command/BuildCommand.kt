package org.nova.cli.command

import org.nova.core.Config
import org.nova.core.MetaInfo
import org.nova.core.Nova
import org.nova.core.NovaBuildLayout
import picocli.CommandLine
import java.io.File
import java.nio.file.Path
import java.util.concurrent.Callable

@CommandLine.Command(
    name = "build", mixinStandardHelpOptions = true, version = ["checksum 4.0"],
    description = ["Build Static Site"]
)
class BuildCommand : Callable<Int> {
    override fun call(): Int {

        val novaBuildLayout = NovaBuildLayout(Path.of(""), Path.of(""))
        Nova(
            novaBuildLayout, Config(
                listOf(File("")),
                listOf(MetaInfo(name = "", content = ""))
            )
        ).build()
        return 0
    }
}