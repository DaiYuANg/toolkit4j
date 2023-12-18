/* (C)2023*/
package org.nectar.nova.core;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.nio.file.Path;
import java.util.List;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.nectar.nova.core.di.DIContainer;
import org.nectar.nova.core.provider.WebJarAssetLocatorProvider;
import org.nectar.nova.core.structure.DocumentSource;
import org.nectar.nova.core.structure.MetaInfo;
import org.nectar.nova.core.structure.SiteInfo;

@Slf4j
@ToString
@Builder
public class Nova {

	@Singular
	private final List<DocumentSource> documentSources;

	@Singular
	private final List<MetaInfo> metaInfos;

	private final SiteInfo siteInfo;

	@Getter
	private final Path output;

	@Builder.Default
	private Injector injector = DIContainer.INSTANCE.getInjector();

	@Inject
	private WebJarAssetLocatorProvider webJarAssetLocatorProvider;

	public void parser() {
		injector.injectMembers(this);
	}
}
