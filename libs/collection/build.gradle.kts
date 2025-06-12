group = "org.toolkit.collection"

dependencies {
  api(libs.fastutil)
  implementation(libs.record.builder.core)
  annotationProcessor(libs.record.builder.processor)
  testImplementation(libs.gson)
}