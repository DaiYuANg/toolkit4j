import io.freefair.gradle.plugins.lombok.LombokPlugin
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
    apply<JavaLibraryPlugin>()
    apply<LombokPlugin>()
    apply<PlantUmlPlugin>()
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies{
        testImplementation("org.jetbrains.kotlin:kotlin-test")
    }

    tasks.test {
        useJUnitPlatform()
    }

    kotlin {
        jvmToolchain(jdkVersion = jdkVersion.get().toInt())
    }
}