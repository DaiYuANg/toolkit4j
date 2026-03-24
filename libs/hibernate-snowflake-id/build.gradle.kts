plugins {
  `java-library`
}

group = "org.toolkit4j"

dependencies {
  compileOnly(libs.hibernate.orm)
  compileOnly(libs.jakarta.persistence.api)
  implementation(libs.agrona)
  testImplementation(libs.hibernate.orm)
  testImplementation(libs.jakarta.persistence.api)
}
