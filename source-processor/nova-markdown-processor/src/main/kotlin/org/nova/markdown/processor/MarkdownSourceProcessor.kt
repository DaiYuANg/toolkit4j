package org.nova.markdown.processor

import com.google.auto.service.AutoService
import com.vladsch.flexmark.ext.autolink.AutolinkExtension
import com.vladsch.flexmark.ext.gfm.issues.GfmIssuesExtension
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.gfm.users.GfmUsersExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.data.MutableDataSet
import com.vladsch.flexmark.util.misc.Extension
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.nova.base.SourceProcessor
import java.util.*


@AutoService(SourceProcessor::class)
class MarkdownSourceProcessor : SourceProcessor {

    private val supportExt = "md"

    private var options: MutableDataSet = MutableDataSet()
        .set(
            Parser.EXTENSIONS,
            listOf(
                StrikethroughExtension.create(),
                GfmUsersExtension.create(),
                GfmIssuesExtension.create(),
                AutolinkExtension.create()
            )
        );
    private var parser: Parser = Parser.builder(options).build()

    override fun processor(source: String): Document {
        val nodes = parser.parse(source)
        val renderer = HtmlRenderer.builder(options).build()
        val html = renderer.render(nodes)
        return Jsoup.parse(html)
    }

    override fun isSupportExt(ext: String): Boolean {
        return ext.contains(supportExt)
    }
}