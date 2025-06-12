plugins {
  idea
  `java-library`
  alias(libs.plugins.jmh)
  alias(libs.plugins.versionCheck)
  alias(libs.plugins.spotless)
  alias(libs.plugins.dokka)
  alias(libs.plugins.git)
}
group = "org.toolkit4j"

allprojects {
  repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    google()
  }
}
subprojects {
  if (project.name != "document") {

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
