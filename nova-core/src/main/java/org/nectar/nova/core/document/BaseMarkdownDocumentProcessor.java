/* (C)2023*/
package org.nectar.nova.core.document;

import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import jakarta.inject.Inject;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Slf4j
public class BaseMarkdownDocumentProcessor implements MarkdownDocumentProcessor {

	private final Executor executor;

	@Override
	public Node processor(String source) {
		MutableDataSet options = new MutableDataSet();
		Parser parser = Parser.builder(options).build();
		return parser.parse(source);
	}

	private void processCodeBlock(@NotNull FencedCodeBlock codeBlock) {
		System.out.println("Found a code block:");
		System.out.println("```" + codeBlock.getInfo() + "\n" + codeBlock.getContentChars() + "\n```");
	}
}
