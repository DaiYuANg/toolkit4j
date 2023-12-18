/* (C)2023*/
package org.nectar.nova.core.di;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import lombok.val;
import org.nectar.nova.core.base.Processor;
import org.nectar.nova.core.document.BaseMarkdownDocumentProcessor;

public class DocumentModule extends AbstractModule {
	@Override
	protected void configure() {
		val multiBinder = Multibinder.newSetBinder(binder(), Processor.class);
		multiBinder.addBinding().to(BaseMarkdownDocumentProcessor.class);
	}
}
