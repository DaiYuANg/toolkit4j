import com.vanniktech.maven.publish.MavenPublishBaseExtension
import me.champeau.jmh.JMHPlugin
import name.remal.gradle_plugins.lombok.LombokPlugin

plugins {
  alias(libs.plugins.dotenv)
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
group = "org.toolkit4j"
version = "0.1.0-SNAPSHOT"

/** Copy selected keys from root `.env` (dotenv plugin) into Gradle extra properties for publishing. */
private fun Project.applyPublishingPropsFromDotenv() {
  val envExt = rootProject.extensions.findByName("env") ?: return
  val fetchOrNull =
    envExt.javaClass.getMethod("fetchOrNull", String::class.java).let { method ->
      { key: String -> method.invoke(envExt, key) as String? }
    }
  val keys =
    listOf(
      "mavenCentralUsername",
      "mavenCentralPassword",
      "signingInMemoryKey",
      "signingInMemoryKeyId",
      "signingInMemoryKeyPassword",
      "POM_URL",
      "POM_NAME",
      "POM_DESCRIPTION",
      "POM_INCEPTION_YEAR",
      "POM_LICENSE_NAME",
      "POM_LICENSE_URL",
      "POM_LICENSE_DIST",
      "POM_SCM_URL",
      "POM_SCM_CONNECTION",
      "POM_SCM_DEV_CONNECTION",
      "POM_DEVELOPER_ID",
      "POM_DEVELOPER_NAME",
      "POM_DEVELOPER_URL",
    )
  keys.forEach { key ->
    val v = fetchOrNull(key)
    if (!v.isNullOrBlank()) {
      extensions.extraProperties.set(key, v)
    }
  }
}

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
    applyPublishingPropsFromDotenv()
    apply<JMHPlugin>()
    apply<LombokPlugin>()
    apply<JavaLibraryPlugin>()
    apply<com.vanniktech.maven.publish.MavenPublishPlugin>()
    group = rootProject.group
    version = rootProject.version

    extensions.configure<MavenPublishBaseExtension>("mavenPublishing") {
      publishToMavenCentral()
      signAllPublications()
      pom {
        name.set(provider { findProperty("POM_NAME") as String? ?: "${rootProject.name}-${project.name}" })
        description.set(provider {
          findProperty("POM_DESCRIPTION") as String? ?: "toolkit4j module ${project.name}"
        })
        inceptionYear.set(provider { findProperty("POM_INCEPTION_YEAR") as String? ?: "2025" })
        url.set(provider { findProperty("POM_URL") as String? ?: "https://github.com/toolkit4j/toolkit4j" })
        licenses {
          license {
            name.set(provider { findProperty("POM_LICENSE_NAME") as String? ?: "MIT" })
            url.set(provider {
              findProperty("POM_LICENSE_URL") as String? ?: "https://opensource.org/licenses/MIT"
            })
            distribution.set(provider { findProperty("POM_LICENSE_DIST") as String? ?: "repo" })
          }
        }
        developers {
          developer {
            id.set(provider { findProperty("POM_DEVELOPER_ID") as String? ?: "toolkit4j" })
            name.set(provider { findProperty("POM_DEVELOPER_NAME") as String? ?: "toolkit4j" })
            url.set(provider {
              findProperty("POM_DEVELOPER_URL") as String? ?: "https://github.com/toolkit4j/toolkit4j"
            })
          }
        }
        scm {
          url.set(provider { findProperty("POM_SCM_URL") as String? ?: "https://github.com/toolkit4j/toolkit4j" })
          connection.set(provider {
            findProperty("POM_SCM_CONNECTION") as String?
              ?: "scm:git:git://github.com/toolkit4j/toolkit4j.git"
          })
          developerConnection.set(provider {
            findProperty("POM_SCM_DEV_CONNECTION") as String?
              ?: "scm:git:ssh://git@github.com/toolkit4j/toolkit4j.git"
          })
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
      val onCi = System.getenv("CI").equals("true", ignoreCase = true)
      if (onCi) {
        minHeapSize = "256m"
        maxHeapSize = "1536m"
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
        systemProperties["junit.jupiter.execution.parallel.enabled"] = false
      } else {
        minHeapSize = "4g"
        maxHeapSize = "8g"
        maxParallelForks = Runtime.getRuntime().availableProcessors() * 2
        systemProperties["junit.jupiter.execution.parallel.enabled"] = true
        systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
      }
      jvmArgs("-XX:+EnableDynamicAgentLoading")
    }
  }
}
