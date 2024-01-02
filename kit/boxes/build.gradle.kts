plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
}

dependencies {}

kotlin {
    jvmToolchain(jdkVersion = libs.versions.jdkVersion.get().toInt())
    compilerOptions { freeCompilerArgs = listOf("-Xjvm-default=all") }
}
