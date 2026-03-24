plugins {
  `java-library`
}

dependencies {
  compileOnly(libs.quartz)
  testImplementation(libs.quartz)
}

