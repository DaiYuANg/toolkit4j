plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.lombok")
}

group = "org.toolkit.spring.boot.cache.caffine"

version = "1.0-SNAPSHOT"

dependencies {
    compileOnly(libs.autoService)
    annotationProcessor(libs.autoService)
    implementation(projects.cache.cacheApi)
    implementation(libs.caffine)
}

