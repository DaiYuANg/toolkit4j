plugins {
  `java-library`
}

group = "org.toolkit4j"
version = "unspecified"

dependencies {
  compileOnly("org.hibernate.orm:hibernate-core:7.2.7.Final")
  compileOnly("jakarta.persistence:jakarta.persistence-api:3.2.0")
  implementation(libs.agrona)
  testImplementation("org.hibernate.orm:hibernate-core:7.2.7.Final")
  testImplementation("jakarta.persistence:jakarta.persistence-api:3.2.0")
}