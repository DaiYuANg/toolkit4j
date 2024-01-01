plugins {
    kotlin("jvm")
    kotlin("kapt")
}

group = "org.toolkit.spring.boot.cache.caffine"

version = "1.0-SNAPSHOT"

dependencies {
    compileOnly(libs.autoService)
    annotationProcessor(libs.autoService)
    kapt(libs.autoService)
    implementation(projects.kit.cacheApi)
    implementation(libs.caffine)
}
