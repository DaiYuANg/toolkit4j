/* (C)2023*/
package org.nectar.nova.cli;

import lombok.val;
import org.nectar.nova.cli.command.RootCommand;
import picocli.CommandLine;

public class NovaCliApplication {
	public static void main(String[] args) {
		val command = new CommandLine(new RootCommand());
		command.execute(args);
	}
}
