import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.Test

private val publishingPropertyKeys =
    listOf(
        "mavenCentralUsername",
        "mavenCentralPassword",
        "signingInMemoryKey",
        "signingInMemoryKeyId",
        "signingInMemoryKeyPassword",
        "POM_URL",
        "POM_NAME",
        "POM_DESCRIPTION",
        "POM_INCEPTION_YEAR",
        "POM_LICENSE_NAME",
        "POM_LICENSE_URL",
        "POM_LICENSE_DIST",
        "POM_SCM_URL",
        "POM_SCM_CONNECTION",
        "POM_SCM_DEV_CONNECTION",
        "POM_DEVELOPER_ID",
        "POM_DEVELOPER_NAME",
        "POM_DEVELOPER_URL",
    )

fun Project.isPublishableLeafModule(): Boolean = name != "document" && childProjects.isEmpty()

fun Project.isBomModule(): Boolean = path == ":libs:toolkit4j-bom"

fun Project.applyPublishingPropsFromDotenv() {
    val envExt = rootProject.extensions.findByName("env") ?: return
    val fetchOrNullMethod = envExt.javaClass.getMethod("fetchOrNull", String::class.java)

    publishingPropertyKeys.forEach { key ->
        val value = fetchOrNullMethod.invoke(envExt, key) as String?
        if (!value.isNullOrBlank()) {
            extensions.extraProperties.set(key, value)
        }
    }
}

fun Project.stringPropertyOrDefault(
    name: String,
    defaultValue: () -> String,
): Provider<String> = provider {
    (findProperty(name) as String?)?.takeIf(String::isNotBlank) ?: defaultValue()
}

fun Test.configureToolkitTestRuntime() {
    val onCi = System.getenv("CI").equals("true", ignoreCase = true)
    if (onCi) {
        minHeapSize = "256m"
        maxHeapSize = "1536m"
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
        systemProperties["junit.jupiter.execution.parallel.enabled"] = false
    } else {
        minHeapSize = "4g"
        maxHeapSize = "8g"
        maxParallelForks = Runtime.getRuntime().availableProcessors() * 2
        systemProperties["junit.jupiter.execution.parallel.enabled"] = true
        systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
    }

    jvmArgs("-XX:+EnableDynamicAgentLoading")
}
