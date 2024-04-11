group = "org.toolkit.spring.boot.cache.redis"

apply<KotlinSetting>()

dependencies {
  implementation(libs.autoFactory)
  annotationProcessor(libs.autoFactory)
  implementation(projects.cache.cacheApi)
  implementation(libs.lettuceCore)
  implementation(libs.apacheCommonPool)
}
