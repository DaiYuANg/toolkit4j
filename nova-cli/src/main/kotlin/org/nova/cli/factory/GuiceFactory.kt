package org.nova.cli.factory

import com.google.inject.ConfigurationException
import org.nova.cli.guice.injector
import picocli.CommandLine
import picocli.CommandLine.IFactory

class GuiceFactory : IFactory {
    override fun <K : Any?> create(p0: Class<K>?): K {
        return try {
            injector.getInstance(p0)
        } catch (ex: ConfigurationException) {
            CommandLine.defaultFactory().create(p0)
        }
    }
}