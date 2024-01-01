import io.freefair.gradle.plugins.lombok.LombokPlugin
import io.gitlab.plunts.gradle.plantuml.plugin.ClassDiagramsExtension
import io.gitlab.plunts.gradle.plantuml.plugin.PlantUmlPlugin
import me.champeau.jmh.JMHPlugin

plugins {
  `version-catalog`
  idea
  `java-library`
  kotlin("jvm")
  alias(libs.plugins.jmh)
  id("io.freefair.lombok") version "8.4"
  id("com.palantir.git-version") version "3.0.0"
  id("io.gitlab.plunts.plantuml") version "2.1.3"
  id("com.diffplug.spotless") version "6.23.3"
  id("org.jetbrains.dokka") version "1.9.10"
}

val jdkVersion = libs.versions.jdkVersion
val plantUMLSuffix = "puml"

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
  apply<JMHPlugin>()
  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "org.jetbrains.dokka")

  dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.8.0-RC2")
    testImplementation(platform(rootProject.libs.junitBom))
    testImplementation(rootProject.libs.junitJuiter)
    testImplementation(rootProject.libs.junitApi)
    testImplementation(rootProject.libs.junitEngine)
    testImplementation(rootProject.libs.junitInjectFile)
    testImplementation(rootProject.libs.mockitoCore)
    testImplementation(rootProject.libs.mockitoJunit)
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation(rootProject.libs.dataFaker)
    testImplementation("org.slf4j:slf4j-api:2.1.0-alpha0")
    testImplementation("org.slf4j:slf4j-simple:2.1.0-alpha0")
  }

  tasks.test { useJUnitPlatform() }

  kotlin {
    jvmToolchain(jdkVersion = jdkVersion.get().toInt())
    compilerOptions { freeCompilerArgs = listOf("-Xjvm-default=all") }
  }

  classDiagrams {
    @Suppress("UNCHECKED_CAST")
    diagram(
        "classes",
        closureOf<ClassDiagramsExtension.ClassDiagram> {
          include(packages().recursive())
          writeTo(file(project.layout.projectDirectory.file("${project.name}.$plantUMLSuffix")))
        }
            as groovy.lang.Closure<ClassDiagramsExtension.ClassDiagram>,
    )
  }
  tasks.test {
    useJUnitPlatform()
    minHeapSize = "4g"
    maxParallelForks = Runtime.getRuntime().availableProcessors() * 2
    maxHeapSize = "8g"
    systemProperties["junit.jupiter.execution.parallel.enabled"] = true
    systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
    jvmArgs("-XX:+EnableDynamicAgentLoading")
    maxParallelForks = Runtime.getRuntime().availableProcessors() * 2
  }
}

spotless {
  java {
    importOrder()
    removeUnusedImports()
    formatAnnotations()
    eclipse()
  }
  kotlin {
    ktfmt()
    ktlint()
    diktat()
  }
  kotlinGradle {
    target("*.gradle.kts")
    ktlint()
    ktfmt()
  }
}
