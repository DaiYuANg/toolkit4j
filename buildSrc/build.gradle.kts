plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
    `embedded-kotlin`
}

repositories {
    maven { setUrl("https://repo.spring.io/snapshot") }
    maven { setUrl("https://repo.spring.io/milestone") }
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    google()
}
val kotlinVersion = libs.versions.kotlin.get();

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-lombok:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-serialization:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-noarg:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-allopen:${kotlinVersion}")
    implementation("com.github.spotbugs.snom:spotbugs-gradle-plugin:6.0.2")
}
