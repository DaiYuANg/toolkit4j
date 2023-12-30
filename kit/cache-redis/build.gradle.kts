group = "org.toolkit.spring.boot.cache.redis"

version = "1.0-SNAPSHOT"

dependencies {
  compileOnly(libs.autoFactory)
  annotationProcessor(libs.autoFactory)
  implementation(libs.lettuceCore)
}
