package org.nova.cli.guice

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector

val injector: Injector = Guice.createInjector(RootModule())

class RootModule: AbstractModule(){

    override fun configure() {
    }
}