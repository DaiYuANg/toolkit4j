plugins { `java-library` }

dependencies {
  compileOnly(libs.quartz)
  compileOnly(libs.record.builder.core)
  annotationProcessor(libs.record.builder.processor)
  testImplementation(libs.quartz)
}
