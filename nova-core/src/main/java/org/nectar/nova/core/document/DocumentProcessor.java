/* (C)2023*/
package org.nectar.nova.core.document;

import org.jsoup.nodes.Document;
import org.nectar.nova.core.base.Processor;

public interface DocumentProcessor extends Processor<String, Document> {

	Document processor(String source);
}
