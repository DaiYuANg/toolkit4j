import io.freefair.gradle.plugins.lombok.LombokPlugin
import io.gitlab.plunts.gradle.plantuml.plugin.ClassDiagramsExtension
import io.gitlab.plunts.gradle.plantuml.plugin.PlantUmlPlugin

plugins {
    `version-catalog`
    idea
    `java-library`
    kotlin("jvm")
    id("io.freefair.lombok") version "8.4"
    id("com.palantir.git-version") version "3.0.0"
    id("io.gitlab.plunts.plantuml") version "2.1.3"
}
val jdkVersion = libs.versions.jdkVersion;

allprojects {
    repositories {
        maven { setUrl("https://repo.spring.io/snapshot") }
        maven { setUrl("https://repo.spring.io/milestone") }
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}


subprojects {
    apply<LombokPlugin>()
    apply<JavaLibraryPlugin>()
    apply<PlantUmlPlugin>()
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies{
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
        runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.8.0-RC2")
        testImplementation(platform(rootProject.libs.junitBom))
        testImplementation(rootProject.libs.junitJuiter)
        testImplementation(rootProject.libs.junitApi)
        testRuntimeOnly(rootProject.libs.junitEngine)
        testImplementation(rootProject.libs.junitInjectFile)
        testImplementation(rootProject.libs.mockitoCore)
        testImplementation(rootProject.libs.mockitoJunit)
        testImplementation("org.jetbrains.kotlin:kotlin-test")
    }

    tasks.test {
        useJUnitPlatform()
    }

    kotlin {
        jvmToolchain(jdkVersion = jdkVersion.get().toInt())
    }

    classDiagrams {
        @Suppress("UNCHECKED_CAST")
        diagram("123",closureOf<ClassDiagramsExtension.ClassDiagram> {
            include(packages().recursive())
            writeTo(file(project.layout.buildDirectory.file("cli.puml")))
        } as groovy.lang.Closure<ClassDiagramsExtension.ClassDiagram>)
    }
}