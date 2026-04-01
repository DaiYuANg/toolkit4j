package org.toolkit4j.data.model.value;

import io.soabase.recordbuilder.core.RecordBuilder;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

@RecordBuilder
public record Option<T>(T value, String label, String tag, List<Option<T>> children) {

  public Option {
    children = List.copyOf(Objects.requireNonNullElse(children, List.of()));
  }

  public boolean hasChildren() {
    return !children.isEmpty();
  }

  public static <T> @NotNull Option<T> of(T value, String label) {
    return new Option<>(value, label, null, List.of());
  }
}
