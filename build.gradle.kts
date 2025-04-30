plugins {
  idea
  `java-library`
  alias(libs.plugins.jmh)
  alias(libs.plugins.versionCheck)
  alias(libs.plugins.spotless)
  alias(libs.plugins.dokka)
  alias(libs.plugins.git)
//  id("com.palantir.git-version") version "3.0.0"
//  id("io.gitlab.plunts.plantuml") version "2.1.3"
//  id("com.diffplug.spotless") version "6.25.0"
//  id("org.jetbrains.dokka") version "1.9.20"
//  id("co.uzzu.dotenv.gradle") version "4.0.0"
}

//val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra
//val details = versionDetails()

group = "org.toolkit4j"

//version = details.gitHash

val jdkVersion = libs.versions.jdkVersion

allprojects {
  repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    google()
  }
}
subprojects {
  if (project.name != "website") {
//    apply<LombokPlugin>()
//    apply<PlantUmlPlugin>()
//    apply<JMHPlugin>()
//    apply<PublishingPlugin>()
//    apply<MavenPublishPlugin>()
//    apply<DokkaPlugin>()

    group = rootProject.group
    version = rootProject.version
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
  }
}

//spotless {
//  java {
//    importOrder()
//    removeUnusedImports()
//    formatAnnotations()
//    eclipse()
//  }
//  kotlin {
//    ktfmt()
//    ktlint()
//    diktat()
//  }
//  kotlinGradle {
//    target("*.gradle.kts")
//    ktlint()
//    ktfmt()
//  }
//}
//
