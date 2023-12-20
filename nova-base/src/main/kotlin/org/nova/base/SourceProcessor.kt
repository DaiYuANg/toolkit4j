package org.nova.base

import org.jsoup.nodes.Document

interface SourceProcessor {
    fun processor(source: String): Document

    fun whoNext(processor: SourceProcessor)
}