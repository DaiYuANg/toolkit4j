plugins {
    kotlin("kapt")
}
group = "org.toolkit.site.generator.cli"

version = "1.0-SNAPSHOT"

dependencies {
    implementation("io.javalin:javalin:6.0.0-beta.3")
    implementation("info.picocli:picocli:4.7.5")
    implementation("info.picocli:picocli-codegen:4.7.5")
}
//
//kapt {
//    arguments {
//        arg("project", "${project.group}/${project.name}")
//    }
//}