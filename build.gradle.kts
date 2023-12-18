import name.remal.gradle_plugins.lombok.LombokPlugin

plugins {
    `version-catalog`
    idea
    `java-library`
    kotlin("jvm")
    id("name.remal.lombok") version "2.2.4" apply false
    id("com.palantir.git-version") version "3.0.0"
}


allprojects{
    repositories {
        maven { setUrl("https://repo.spring.io/snapshot") }
        maven { setUrl("https://repo.spring.io/milestone") }
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

subprojects{
    apply<JavaLibraryPlugin>()
    apply<LombokPlugin>()
}