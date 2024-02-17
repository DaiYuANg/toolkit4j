plugins {
  `kotlin-project`
}

group = "org.toolkit.collection"

version = "1.0-SNAPSHOT"

dependencies {
  api("org.eclipse.collections:eclipse-collections-api:11.1.0")
  api("org.eclipse.collections:eclipse-collections:11.1.0")
  api("com.google.guava:guava:33.0.0-jre")
  api("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.6")
  api(libs.fastutil)
  testImplementation(projects.kit.ktslf4j)
  testImplementation(kotlin("test"))
}


