package org.nova.cli.guice

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import io.javalin.Javalin

val injector: Injector = Guice.createInjector(RootModule())

class RootModule: AbstractModule(){

    override fun configure() {
    }
}