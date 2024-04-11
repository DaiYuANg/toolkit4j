import io.gitlab.plunts.gradle.plantuml.plugin.ClassDiagramsExtension
import io.gitlab.plunts.gradle.plantuml.plugin.PlantUmlPlugin
import me.champeau.jmh.JMHPlugin
import name.remal.gradle_plugins.lombok.LombokPlugin
import org.jetbrains.dokka.gradle.DokkaPlugin

plugins {
  `version-catalog`
  idea
  `java-library`
  kotlin("jvm")
  kotlin("kapt")
  `maven-publish`
  alias(libs.plugins.jmh)
  alias(libs.plugins.versionCheck)
  alias(libs.plugins.lombok)
  id("com.palantir.git-version") version "3.0.0"
  id("io.gitlab.plunts.plantuml") version "2.1.3"
  id("com.diffplug.spotless") version "6.25.0"
  id("org.jetbrains.dokka") version "1.9.20"
  id("co.uzzu.dotenv.gradle") version "4.0.0"
}

val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra
val details = versionDetails()

group = "org.toolkit4j"

version = details.gitHash
val rlibs = rootProject.libs;

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
val gitLabPrivateToken: String by project
subprojects {
  if (project.name != "website") {
    apply<LombokPlugin>()
    apply<JavaLibraryPlugin>()
    apply<PlantUmlPlugin>()
    apply<JMHPlugin>()
    apply<PublishingPlugin>()
    apply<MavenPublishPlugin>()
    apply<DokkaPlugin>()

    group = rootProject.group
    version = rootProject.version

    dependencies {
      compileOnly(rlibs.jetbrainsAnnotation)
      testImplementation(platform(rlibs.junitBom))
      testImplementation(rlibs.junitJuiter)
      testImplementation(rlibs.junitApi)
      testImplementation(rlibs.junitEngine)
      testImplementation(rlibs.junitInjectFile)
      testImplementation(rlibs.mockitoCore)
      testImplementation(rlibs.mockitoJunit)
      testImplementation(rlibs.dataFaker)
      testImplementation(rlibs.slf4j)
    }

    classDiagrams {
      @Suppress("UNCHECKED_CAST") diagram(
        "classes",
        closureOf<ClassDiagramsExtension.ClassDiagram> {
          include(packages().recursive())
          writeTo(file(project.layout.buildDirectory.file("${project.name}.$plantUMLSuffix")))
        } as groovy.lang.Closure<ClassDiagramsExtension.ClassDiagram>,
      )
    }

    java {
      withJavadocJar()
      withSourcesJar()
      modularity.inferModulePath.set(true)
    }

    tasks.jar {
      manifest {
        attributes("Version" to project.version)
      }
      duplicatesStrategy = DuplicatesStrategy.INCLUDE
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

    tasks.register<Jar>("dokkaHtmlJar") {
      dependsOn(tasks.dokkaHtml)
      from(tasks.dokkaHtml.flatMap { it.outputDirectory })
      archiveClassifier.set("html-docs")
    }

    tasks.register<Jar>("dokkaJavadocJar") {
      dependsOn(tasks.dokkaJavadoc)
      from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
      archiveClassifier.set("javadoc")
    }
    publishing {
      publications {
        create<MavenPublication>("library") {
          groupId = project.group.toString()
          artifactId = project.name
          version = project.version.toString()

          from(components["java"])

          pom {
            developers {
              developer {
                id = "DaiYuANg"
                name = "DaiYuANg"
              }
            }
          }
        }
      }
      repositories {

        maven {
          url = uri(env.PUBLISH_URL.value)
          name = env.PUBLISHP_NAME.value
          credentials {
            username = env.USERNAME.value
            password = env.PASSWORD.value
          }
          authentication {
            create("basic", BasicAuthentication::class)
          }
        }
      }
    }
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

