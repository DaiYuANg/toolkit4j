plugins {
  kotlin("jvm")
  kotlin("plugin.allopen")
}

group = "org.toolkit.collections"

version = "1.0-SNAPSHOT"

dependencies {
  api("org.eclipse.collections:eclipse-collections-api:11.1.0")
  api("org.eclipse.collections:eclipse-collections:11.1.0")
  api("com.google.guava:guava:33.0.0-jre")
  api("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.6")
}

kotlin {
  jvmToolchain(21)

}
