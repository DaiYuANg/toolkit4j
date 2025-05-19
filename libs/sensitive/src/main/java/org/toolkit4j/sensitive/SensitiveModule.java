package org.toolkit4j.sensitive;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.jetbrains.annotations.NotNull;

public class SensitiveModule extends SimpleModule {
  @Override
  public void setupModule(@NotNull SetupContext context) {
    context.addBeanSerializerModifier(new SensitiveBeanSerializerModifier());
  }
}
