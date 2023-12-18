package org.nova.cli

import org.nova.cli.command.RootCommand
import org.nova.cli.factory.GuiceFactory
import picocli.CommandLine
import kotlin.system.exitProcess


fun main(args: Array<String>): Unit = exitProcess(CommandLine(RootCommand(), GuiceFactory()).execute(*args))