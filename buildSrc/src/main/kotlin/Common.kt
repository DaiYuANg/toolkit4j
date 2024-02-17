import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the


fun libs(project: Project): LibrariesForLibs {
    return project.the<LibrariesForLibs>()
}

fun rootProject(project: Project): Project {
    return if (project.parent == null) {
        project
    } else {
        rootProject(project.parent!!)
    }
}

fun rootLibs(project: Project): LibrariesForLibs {
    return libs(rootProject(project))
}