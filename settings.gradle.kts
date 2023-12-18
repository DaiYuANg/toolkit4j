pluginManagement {
    repositories {
        maven { setUrl("https://repo.spring.io/snapshot") }
        maven { setUrl("https://repo.spring.io/milestone") }
//    maven { setUrl("https://jitpack.io") }
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
    }

    plugins {
        val gitPluginVersion: String by settings
        val spotbugsBaseVersion: String by settings
        val spotlessPluginVersion: String by settings
        val dependencycheckVersion: String by settings
        val jmhPluginVersion: String by settings
        val jreleaserVersion: String by settings
        val lombokPluginVersion: String by settings
        val nodePluginVersion: String by settings
        val springBootVersion: String by settings
        val springDependencyManagementVersion: String by settings
        val gradlePreCommitGitGooksVersion: String by settings
        val webjarVersion: String by settings
        val kotlinVersion: String by settings
        id("org.danilopianini.gradle-pre-commit-git-hooks") version "1.1.9"
        id("com.gradle.enterprise") version "3.13.4"
        id("com.palantir.git-version") version gitPluginVersion
        id("org.owasp.dependencycheck") version dependencycheckVersion
        id("me.champeau.jmh") version jmhPluginVersion
        id("io.freefair.lombok") version lombokPluginVersion
        id("org.jreleaser") version jreleaserVersion
        id("com.github.node-gradle.node") version nodePluginVersion
        id("com.github.ben-manes.versions") version "0.47.0"
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
        id("org.danilopianini.gradle-pre-commit-git-hooks") version gradlePreCommitGitGooksVersion
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

plugins {
    id("com.gradle.enterprise")
    id("org.danilopianini.gradle-pre-commit-git-hooks")
}

buildCache {
    local {
        isEnabled = true
        directory = File(rootProject.projectDir, ".gradle/build-cache")
    }
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

gitHooks {
    //    preCommit {
    //        logger.log(LogLevel.INFO,"pre commit")
    //    }
    //    createHooks()
}

rootProject.name = "Nova"

include("nova-core")

include("nova-cli")
include("nova-gradle-plugin")
