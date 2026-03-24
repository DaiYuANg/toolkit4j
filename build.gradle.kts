import me.champeau.jmh.JMHPlugin
import name.remal.gradle_plugins.lombok.LombokPlugin

plugins {
  idea
  `java-library`
  alias(libs.plugins.jmh)
  alias(libs.plugins.version.check)
  alias(libs.plugins.spotless)
  alias(libs.plugins.dokka)
  alias(libs.plugins.git)
  alias(libs.plugins.lombok)
  alias(libs.plugins.plantuml)
  alias(libs.plugins.maven.publish)
}

/** Maven coordinates; Central GitHub namespace is typically io.github.{lowercase login}. */
group = "io.github.daiyuang"

version = "0.1.0"

allprojects {
  repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    google()
  }
}

val rlibs = rootProject.libs;

subprojects {
  if (project.name != "document") {
    apply<JMHPlugin>()
    apply<LombokPlugin>()
    apply<JavaLibraryPlugin>()
    apply<com.vanniktech.maven.publish.MavenPublishPlugin>()
    group = rootProject.group
    version = rootProject.version

    mavenPublishing {
      publishToMavenCentral()
      signAllPublications()
      pom {
        name.set("toolkit4j-${project.name}")
        description.set(
          "Lightweight JVM utility toolkit — module \"${project.name}\". See https://github.com/DaiYuANg/toolkit4j",
        )
        inceptionYear.set("2026")
        url.set("https://github.com/DaiYuANg/toolkit4j")
        licenses {
          license {
            name.set("The Apache License, Version 2.0")
            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            distribution.set("repo")
          }
        }
        developers {
          developer {
            id.set("daiyuang")
            name.set("DaiYuANg")
            url.set("https://github.com/DaiYuANg")
          }
        }
        scm {
          url.set("https://github.com/DaiYuANg/toolkit4j")
          connection.set("scm:git:https://github.com/DaiYuANg/toolkit4j.git")
          developerConnection.set("scm:git:ssh://git@github.com/DaiYuANg/toolkit4j.git")
        }
      }
    }
//    tasks.register<Jar>("dokkaHtmlJar") {
//      dependsOn(tasks.dokkaHtml)
//      from(tasks.dokkaHtml.flatMap { it.outputDirectory })
//      archiveClassifier.set("html-docs")
//    }
//
//    tasks.register<Jar>("dokkaJavadocJar") {
//      dependsOn(tasks.dokkaJavadoc)
//      from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
//      archiveClassifier.set("javadoc")
//    }

    dependencies {
      compileOnly(rlibs.jetbrainsAnnotation)
      compileOnly(rlibs.slf4j)
//
      testImplementation(enforcedPlatform(rlibs.junitBom))
      testImplementation(rlibs.junitJuiter)
      testImplementation(rlibs.junitApi)
      testImplementation(rlibs.junitEngine)
      testImplementation(rlibs.junitInjectFile)
      testRuntimeOnly(rlibs.junit.platform.launcher)
      testImplementation(rlibs.mockitoCore)
      testImplementation(rlibs.mockitoJunit)
      testImplementation(rlibs.dataFaker)
      testImplementation(rlibs.slf4j)
      testImplementation(rlibs.slf4j.simple)
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
  }
}
