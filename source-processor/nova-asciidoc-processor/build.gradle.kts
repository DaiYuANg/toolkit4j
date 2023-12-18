plugins {
    kotlin("jvm")
}

group = "org.supervisor.markdown.processor"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.asciidoctor:asciidoctorj:2.5.10")
    implementation("org.asciidoctor:asciidoctorj-pdf:2.3.9")
    implementation("org.asciidoctor:asciidoctorj-diagram:2.2.13")
}

