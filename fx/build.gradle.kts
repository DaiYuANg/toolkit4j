plugins{
    id("org.openjfx.javafxplugin") version "0.1.0"
}

dependencies{
    api(projects.fx.fxDebugger)
    api(projects.fx.fxComponent)
}

subprojects{
    apply{
        plugin("org.openjfx.javafxplugin")
    }
}