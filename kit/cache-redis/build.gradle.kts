group = "org.toolkit.spring.boot.cache.redis"

version = "1.0-SNAPSHOT"

dependencies {
  implementation("jakarta.inject:jakarta.inject-api:2.0.1")
  implementation(libs.autoFactory)
  annotationProcessor(libs.autoFactory)
  implementation(projects.kit.cacheApi)
  implementation(libs.lettuceCore)
  implementation("org.apache.commons:commons-pool2:2.12.0")
}
