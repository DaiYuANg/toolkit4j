apply<KotlinSetting>()

group = "org.toolkit.spring.boot.cache.caffeine"

version = "1.0-SNAPSHOT"

dependencies {
    compileOnly(libs.autoService)
    annotationProcessor(libs.autoService)
    implementation(projects.cache.cacheApi)
    implementation(libs.caffine)
}

