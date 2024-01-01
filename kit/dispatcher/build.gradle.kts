plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(jdkVersion = libs.versions.jdkVersion.get().toInt())
    compilerOptions { freeCompilerArgs = listOf("-Xjvm-default=all") }
}
