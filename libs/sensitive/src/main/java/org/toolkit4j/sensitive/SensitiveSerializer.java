package org.toolkit4j.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SensitiveSerializer extends JsonSerializer<Object> {
  private final Sensitive.Type type;

  public SensitiveSerializer(Sensitive.Type type) {
    this.type = type;
  }

  @Override
  public void serialize(Object value, @NotNull JsonGenerator gen, SerializerProvider serializers) throws IOException {
    String desensitized = SensitiveUtils.desensitize(value.toString(), type);
    gen.writeString(desensitized);
  }
}