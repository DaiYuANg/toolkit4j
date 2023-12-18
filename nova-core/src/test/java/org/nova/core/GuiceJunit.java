/* (C)2023*/
package org.nova.core;

import com.google.inject.Injector;
import org.junit.Before;
import org.nova.core.di.DIContainer;

public class GuiceJunit {
	protected final Injector injector = DIContainer.INSTANCE.getInjector();

	@Before
	public void beforeAll() {
		injector.injectMembers(this);
	}
}
