import io.freefair.gradle.plugins.lombok.LombokPlugin
import io.gitlab.plunts.gradle.plantuml.plugin.ClassDiagramsExtension
import io.gitlab.plunts.gradle.plantuml.plugin.PlantUmlPlugin
import me.champeau.jmh.JMHPlugin
import org.jetbrains.dokka.gradle.DokkaPlugin

plugins {
    `version-catalog`
    idea
    `java-library`
    kotlin("jvm")
    kotlin("kapt")
    `maven-publish`
    alias(libs.plugins.jmh)
    id("io.freefair.lombok") version "8.4"
    id("com.palantir.git-version") version "3.0.0"
    id("io.gitlab.plunts.plantuml") version "2.1.3"
    id("com.diffplug.spotless") version "6.23.3"
    id("org.jetbrains.dokka") version "1.9.10"
    id ("com.github.ben-manes.versions") version "0.51.0"
    id("co.uzzu.dotenv.gradle") version "4.0.0"
}

val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra
val details = versionDetails()

group = "org.toolkit4j"

version = details.gitHash

val jdkVersion = libs.versions.jdkVersion
val plantUMLSuffix = "puml"

allprojects {
    repositories {
        maven { setUrl("https://repo.spring.io/snapshot") }
        maven { setUrl("https://repo.spring.io/milestone") }
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}
val gitLabPrivateToken:String by project
subprojects {
    if (project.name != "website"){
        apply<LombokPlugin>()
        apply<JavaLibraryPlugin>()
        apply<PlantUmlPlugin>()
        apply<JMHPlugin>()
        apply<PublishingPlugin>()
        apply<MavenPublishPlugin>()
        apply<DokkaPlugin>()
        apply(plugin = "org.jetbrains.kotlin.jvm")
        apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
        apply(plugin = "org.jetbrains.kotlin.plugin.lombok")
        apply(plugin = "org.jetbrains.kotlin.kapt")

        group = rootProject.group
        version = rootProject.version

        dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
            implementation(kotlin("stdlib"))
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.8.0-RC2")
            testImplementation(platform(rootProject.libs.junitBom))
            testImplementation(rootProject.libs.junitJuiter)
            testImplementation(rootProject.libs.junitApi)
            testImplementation(rootProject.libs.junitEngine)
            testImplementation(rootProject.libs.junitInjectFile)
            testImplementation(rootProject.libs.mockitoCore)
            testImplementation(rootProject.libs.mockitoJunit)
            testImplementation(rootProject.libs.dataFaker)
            testImplementation("org.slf4j:slf4j-api:2.1.0-alpha0")
            testImplementation("com.github.noconnor:junitperf:1.35.0")
            testImplementation("com.github.noconnor:junitperf-junit5:1.35.0")
            testImplementation(kotlin("test"))
        }

        classDiagrams {
            @Suppress("UNCHECKED_CAST")
            diagram(
                "classes",
                closureOf<ClassDiagramsExtension.ClassDiagram> {
                    include(packages().recursive())
                    writeTo(file(project.layout.buildDirectory.file("${project.name}.$plantUMLSuffix")))
                }
                        as groovy.lang.Closure<ClassDiagramsExtension.ClassDiagram>,
            )
        }

        java {
            withJavadocJar()
            withSourcesJar()
            modularity.inferModulePath.set(true)
        }

        tasks.jar {
            manifest {
                attributes("Version" to project.version)
            }
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }

        kapt {
            keepJavacAnnotationProcessors = true
        }

        kotlin {
            jvmToolchain(jdkVersion = rootProject.libs.versions.jdkVersion.get().toInt())
            compilerOptions {
                freeCompilerArgs = listOf("-Xjvm-default=all")
            }
        }

        val moduleName = "${project.group}.${project.name}"

        tasks.compileJava {
            options.compilerArgumentProviders.add(CommandLineArgumentProvider {
                // Provide compiled Kotlin classes to javac – needed for Java/Kotlin mixed sources to work
                listOf("--patch-module", "$moduleName=${sourceSets["main"].output.asPath}")
            })
        }

        tasks.test {
            useJUnitPlatform()
            minHeapSize = "4g"
            maxParallelForks = Runtime.getRuntime().availableProcessors() * 2
            maxHeapSize = "8g"
            systemProperties["junit.jupiter.execution.parallel.enabled"] = true
            systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
            jvmArgs("-XX:+EnableDynamicAgentLoading")
            maxParallelForks = Runtime.getRuntime().availableProcessors() * 2
        }

        tasks.register<Jar>("dokkaHtmlJar") {
            dependsOn(tasks.dokkaHtml)
            from(tasks.dokkaHtml.flatMap { it.outputDirectory })
            archiveClassifier.set("html-docs")
        }

        tasks.register<Jar>("dokkaJavadocJar") {
            dependsOn(tasks.dokkaJavadoc)
            from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
            archiveClassifier.set("javadoc")
        }
        publishing {
            publications {
                create<MavenPublication>("library") {
                    groupId = project.group.toString()
                    artifactId = project.name
                    version = project.version.toString()

                    from(components["java"])

                    pom {
                        developers {
                            developer {
                                id = "DaiYuANg"
                                name = "DaiYuANg"
                            }
                        }
                    }
                }
            }
            repositories {

                maven {
                    url = uri(env.PUBLISH_URL.value)
                    name = env.PUBLISHP_NAME.value
                    credentials {
                        username = env.USERNAME.value
                        password = env.PASSWORD.value
                    }
                    authentication {
                        create("basic", BasicAuthentication::class)
                    }
                }
            }
        }
    }
}

spotless {
    java {
        importOrder()
        removeUnusedImports()
        formatAnnotations()
        eclipse()
    }
    kotlin {
        ktfmt()
        ktlint()
        diktat()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
        ktfmt()
    }
}

//project("website") { tasks.build { dependsOn(":kit:dokkaGfmMultiModule") } }
