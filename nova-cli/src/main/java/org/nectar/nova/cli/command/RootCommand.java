/* (C)2023*/
package org.nectar.nova.cli.command;

import picocli.CommandLine;

@CommandLine.Command(subcommands = {BuildCommand.class, ServeCommand.class})
public class RootCommand {}
