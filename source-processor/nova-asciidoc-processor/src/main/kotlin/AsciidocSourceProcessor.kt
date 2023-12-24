package org.supervisor.markdown.processor

import com.google.auto.service.AutoService
import org.jsoup.nodes.Document
import org.nova.base.SourceProcessor

@AutoService(SourceProcessor::class)
class AsciidocSourceProcessor :SourceProcessor{
    override fun processor(source: String): Document {
        TODO("Not yet implemented")
    }

    override fun whoNext(processor: SourceProcessor) {
        TODO("Not yet implemented")
    }
}