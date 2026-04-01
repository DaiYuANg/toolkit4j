rootProject.name = "toolkit4j-build-logic"

dependencyResolutionManagement {
  versionCatalogs { create("libs") { from(files("../gradle/libs.versions.toml")) } }
}
