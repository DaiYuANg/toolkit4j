package org.nova.core

import freemarker.template.Configuration
import io.github.classgraph.ClassGraph
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.nova.base.SourceProcessor
import org.nova.core.processor.ImageProcessor
import org.nova.core.processor.VideoProcessor
import org.webjars.WebJarAssetLocator
import java.nio.file.Path
import java.util.ServiceLoader

class NovaBuildLayout(
    val output: Path,
    val sourcePath:Path
)

val bundleProcessor = module {
    single { ImageProcessor() }
    single { VideoProcessor() }
}

val rootModule = module {
    single { ClassGraph() }
    single { WebJarAssetLocator(get<ClassGraph>()) }
    single { Configuration(Configuration.getVersion()) }
}

class Nova(
    val layout: NovaBuildLayout,
    private var config: Config
) {
    private val documentProcessor = ServiceLoader.load(SourceProcessor::class.java)

    init {
        documentProcessor.forEach {
            System.err.println(it)
        }
        startKoin {
            modules(rootModule, bundleProcessor)
        }
    }

    fun build() {
        System.err.println(documentProcessor)
    }
}