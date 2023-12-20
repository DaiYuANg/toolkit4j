plugins {
  id("io.freefair.sass-java") version "8.4"
  kotlin("jvm")
  kotlin("kapt")
}

group = "org.nectar.nova.core"

version = "1.0-SNAPSHOT"

dependencies {
  implementation("org.freemarker:freemarker:2.3.32")
  implementation("com.helger:ph-css:7.0.1")
  implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")
  implementation("io.insert-koin:koin-core:3.5.0")
  implementation("org.webjars:font-awesome:6.4.2")
  implementation("org.webjars.npm:animate.css:4.1.1")
  implementation("org.webjars:webjars-locator:0.50")
  implementation("org.webjars.npm:prismjs:1.29.0")
  implementation("org.webjars.npm:bootstrap:5.3.2")
  implementation("org.graalvm.js:js:23.0.2")
  implementation("org.graalvm.js:js-launcher:23.1.1")
  implementation("de.larsgrefer.sass:sass-embedded-host:3.3.1")
  implementation("de.larsgrefer.sass:sass-embedded-bundled:3.3.1")
  implementation("de.larsgrefer.sass:sass-embedded-protocol:3.3.1")
  implementation("org.webjars.npm:katex:0.16.9")
  //    implementation("org.webjars.npm:mermaid:10.6.1")
  implementation(libs.classgraph)
  implementation(projects.novaBase)
  implementation("net.sourceforge.plantuml:plantuml-mit:1.2023.13")
  testImplementation("io.hosuaby:inject-resources-junit-jupiter:0.3.3")
  testImplementation("ch.qos.logback:logback-classic:1.4.14")
}

