plugins {
    id("org.siouan.frontend-jdk17") version "8.0.0"
}

frontend{
    nodeVersion.set("20.9.0")
}


tasks.register("copyDocumentation") {
    doLast {
        // 拷贝 kit 项目生成的文档到指定目录
        project(":kit").tasks.getByName("dokkaGfmMultiModule").outputs.files.forEach { documentationFile ->
            copy {
                from(documentationFile)
                into("${project.layout.projectDirectory}/javadoc/") // 你希望拷贝到的目录
            }
        }
    }
}