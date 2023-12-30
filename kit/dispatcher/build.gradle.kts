plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
}

dependencies {
}

kotlin {
    jvmToolchain(21)
    compilerOptions { freeCompilerArgs = listOf("-Xjvm-default=all") }
}
