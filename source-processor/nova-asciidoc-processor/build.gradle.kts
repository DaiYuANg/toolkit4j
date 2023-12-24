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
    compileOnly(projects.novaBase)
    compileOnly("com.google.auto.service:auto-service:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")
    implementation("org.asciidoctor:asciidoctorj:2.5.10")
    implementation("org.asciidoctor:asciidoctorj-pdf:2.3.9")
    implementation("org.asciidoctor:asciidoctorj-diagram:2.2.13")
    testImplementation(projects.novaBase)
}

