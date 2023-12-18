plugins {
  kotlin("jvm")
  `java-gradle-plugin`
}

group = "org.nectar.nova.gradle"

version = "1.0.0"

dependencies {
  implementation(kotlin("stdlib"))
//  implementation(projects.tools.novaCore)
  implementation(gradleApi())
  testImplementation(gradleTestKit())
}

gradlePlugin {
  plugins.create(group.toString()) {
    id = group.toString()
    implementationClass = "org.nectar.nova.gradle.plugin.NovaPluginKt"
  }
}

//publishing {
//  publications { create<MavenPublication>("plugin") { from(components["java"]) } }
//  repositories { mavenLocal() }
//}
