package org.nova.markdown.processor;

import com.adelean.inject.resources.junit.jupiter.GivenTextResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import lombok.val;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Documented;

import static org.mockito.Mockito.spy;

@TestWithResources
class MarkdownSourceProcessorTest {

    @GivenTextResource("TestBaseMarkdownProcessorTestCase.md")
    public String testMarkdown;

    private final MarkdownSourceProcessor markdownSourceProcessor = spy(new MarkdownSourceProcessor());

    @Test
    void processor() {
        Document document = markdownSourceProcessor.processor(testMarkdown);
    }
}