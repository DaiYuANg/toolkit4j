/* (C)2023*/
package org.nectar.nova.core.di;

import com.google.inject.AbstractModule;
import io.github.classgraph.ClassGraph;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.val;
import org.nectar.nova.core.provider.WebJarAssetLocatorProvider;
import org.webjars.WebJarAssetLocator;

public class RootModule extends AbstractModule {
	@Override
	protected void configure() {
		val factory = Thread.ofVirtual().name("Nova-", 0L).factory();
		bind(Executor.class).toInstance(Executors.newThreadPerTaskExecutor(factory));
		bind(ClassGraph.class).toInstance(new ClassGraph().enableAllInfo().enableRealtimeLogging());
		bind(WebJarAssetLocator.class).toProvider(WebJarAssetLocatorProvider.class);
	}
}
