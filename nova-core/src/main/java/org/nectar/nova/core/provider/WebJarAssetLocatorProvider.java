/* (C)2023*/
package org.nectar.nova.core.provider;

import io.github.classgraph.ClassGraph;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import lombok.RequiredArgsConstructor;
import org.webjars.WebJarAssetLocator;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class WebJarAssetLocatorProvider implements Provider<WebJarAssetLocator> {

	private final ClassGraph classGraph;

	@Override
	public WebJarAssetLocator get() {
		return new WebJarAssetLocator(classGraph);
	}
}
