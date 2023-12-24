plugins {
  id("io.freefair.sass-java") version "8.4"
  kotlin("kapt")
}

group = "org.nectar.nova.core"

version = "1.0-SNAPSHOT"

dependencies {
  implementation("org.freemarker:freemarker:2.3.32")
  implementation("com.helger:ph-css:7.0.1")
  implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")
  implementation("io.insert-koin:koin-core:3.5.0")
  //Fill this in with the version of kotlinx in use in your project
  val kotlinxHtmlVersion = "0.9.1"
//  implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinxHtmlVersion")
//  implementation("org.jetbrains.kotlinx:kotlinx-html-js:$kotlinxHtmlVersion")
//  implementation("org.jetbrains.kotlinx:kotlinx-html:$kotlinxHtmlVersion")
  implementation("org.graalvm.js:js:23.0.2")
  implementation("org.graalvm.js:js-launcher:23.1.1")
  implementation("de.larsgrefer.sass:sass-embedded-host:3.3.1")
  implementation("de.larsgrefer.sass:sass-embedded-bundled:3.3.1")
  implementation("de.larsgrefer.sass:sass-embedded-protocol:3.3.1")
  implementation(projects.novaBase)
  implementation("commons-io:commons-io:2.15.1")
  implementation("net.sourceforge.plantuml:plantuml-mit:1.2023.13")
  testImplementation("ch.qos.logback:logback-classic:1.4.14")
}

