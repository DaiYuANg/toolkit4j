import me.champeau.jmh.JMHPlugin
import name.remal.gradle_plugins.lombok.LombokPlugin

plugins {
  alias(libs.plugins.jmh)
  alias(libs.plugins.lombok)
  alias(libs.plugins.plantuml)
  alias(libs.plugins.dokka)
  alias(libs.plugins.maven.publish)
}

val rlibs = rootProject.libs;
subprojects {
  apply<JMHPlugin>()
  apply<LombokPlugin>()
  apply<JavaLibraryPlugin>()
  apply<com.vanniktech.maven.publish.MavenPublishPlugin>()

  dependencies {
    implementation(rlibs.jetbrainsAnnotation)
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
