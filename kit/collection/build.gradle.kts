apply<KotlinSetting>()
group = "org.toolkit.collection"

dependencies {
  api(libs.guava)
  api(libs.jgrapht)
  api(libs.eclipseCollections)
  api(libs.eclipseCollectionsAPI)
  api("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.6")
  api(libs.fastutil)
}