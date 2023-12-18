import io.gitlab.plunts.gradle.plantuml.plugin.ClassDiagramsExtension

plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.lombok")
}
group = "org.toolkit.site.generator.cli"

version = "1.0-SNAPSHOT"

dependencies {
    implementation("io.javalin:javalin:6.0.0-beta.3")
    implementation("info.picocli:picocli:4.7.5")
    implementation("info.picocli:picocli-codegen:4.7.5")
    implementation("com.google.inject:guice:7.0.0")
    implementation("ch.qos.logback:logback-classic:1.4.14")
}

classDiagrams {
    @Suppress("UNCHECKED_CAST")
    diagram("123",closureOf<ClassDiagramsExtension.ClassDiagram> {
        include(packages().recursive())
        writeTo(file(project.layout.buildDirectory.file("cli.puml")))
    } as groovy.lang.Closure<ClassDiagramsExtension.ClassDiagram>)
}

//
//kapt {
//    arguments {
//        arg("project", "${project.group}/${project.name}")
//    }
//}