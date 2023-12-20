plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.allopen")
}

dependencies {
    compileOnly(projects.novaBase)
    compileOnly("com.google.auto.service:auto-service:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")
    implementation("com.vladsch.flexmark:flexmark-all:0.64.8")
}