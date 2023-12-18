plugins {
  id("io.freefair.sass-java") version "8.4"
  antlr
}

//apply<GuicePlugin>()

group = "org.nectar.nova.core"

version = "1.0-SNAPSHOT"

dependencies {
  implementation("com.vladsch.flexmark:flexmark-all:0.64.8")
  implementation("org.asciidoctor:asciidoctorj:2.5.10")
  implementation("org.asciidoctor:asciidoctorj-pdf:2.3.9")
  implementation("org.asciidoctor:asciidoctorj-diagram:2.2.13")
  implementation("org.freemarker:freemarker:2.3.32")
  implementation("org.jsoup:jsoup:1.17.1")
  implementation("com.helger:ph-css:7.0.1")
  implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")
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
  implementation("net.sourceforge.plantuml:plantuml-mit:1.2023.13")
  testImplementation("io.hosuaby:inject-resources-junit-jupiter:0.3.3")
  testImplementation("ch.qos.logback:logback-classic:1.4.14")
}

//publishing {
//  publications { create<MavenPublication>("mavenJava") { from(components["java"]) } }
//  repositories { mavenLocal() }
//}
