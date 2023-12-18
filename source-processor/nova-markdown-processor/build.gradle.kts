plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.allopen")
}

group = "org.supervisor.markdown.processor"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.google.auto.service:auto-service:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")
    implementation("com.vladsch.flexmark:flexmark-all:0.64.8")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}