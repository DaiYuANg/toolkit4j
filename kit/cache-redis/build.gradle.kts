group = "org.toolkit.spring.boot.cache.redis"

version = "1.0-SNAPSHOT"

dependencies {
  // https://mvnrepository.com/artifact/jakarta.inject/jakarta.inject-api
  implementation("jakarta.inject:jakarta.inject-api:2.0.1")
  implementation(libs.autoFactory)
  annotationProcessor(libs.autoFactory)
  implementation(libs.lettuceCore)
}
