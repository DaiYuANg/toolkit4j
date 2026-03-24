plugins {
  `java-library`
}

group = "org.toolkit4j"

dependencies {
  compileOnly(libs.quartz)
  testImplementation(libs.quartz)
}

