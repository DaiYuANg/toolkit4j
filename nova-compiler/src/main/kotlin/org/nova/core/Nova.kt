package org.nova.core

import freemarker.template.Configuration
import freemarker.template.Template
import org.apache.commons.io.FileUtils
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.nova.base.SourceProcessor
import org.nova.core.processor.ImageProcessor
import org.nova.core.processor.VideoProcessor
import java.io.File
import java.io.StringWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.*


class NovaBuildLayout(
    val output: Path,
    val sourcePath: Path
)

val bundleProcessor = module {
    single { ImageProcessor() }
    single { VideoProcessor() }
}

val rootModule = module {
    single { Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS) }
}

class Nova(
    private val layout: NovaBuildLayout,
    private var config: Config
) {
    private val documentProcessor = ServiceLoader.load(SourceProcessor::class.java)
    private val application = startKoin {
        modules(rootModule, bundleProcessor)
    }

    init {

    }

    fun build() {
        val cfg = application.koin.get<Configuration>()
        cfg.setClassForTemplateLoading(Nova::class.java, "/")
        val template: Template = cfg.getTemplate("template.ftl")
        val out = StringWriter()
        layout.sourcePath.toFile().walkTopDown().forEach { source ->
            if (source.isDirectory) return@forEach
            val processor = documentProcessor.firstOrNull { it.isSupportExt(source.extension) }
            val sourceCode = FileUtils.readFileToString(source, StandardCharsets.UTF_8)
            val content = processor?.processor(sourceCode)
            val data = mapOf("content" to content?.toString())
            template.process(data,out)
            val f = Path.of(layout.output.toFile().absolutePath+ "${source.nameWithoutExtension}.html").toFile()
            System.err.println(f.absolutePath)
            FileUtils.writeStringToFile(f,out.toString(),StandardCharsets.UTF_8)
        }
    }
}