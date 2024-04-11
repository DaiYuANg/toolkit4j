group = "org.toolkit.spring.boot.cache.hazelcast"

version = "1.0-SNAPSHOT"

apply<KotlinSetting>()

dependencies {
    implementation(libs.autoFactory)
    annotationProcessor(libs.autoFactory)
    api(libs.hazelcast)
}
