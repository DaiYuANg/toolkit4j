package org.toolkit4j.data.model.value;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record KeyValue<K, V>(
  K key,
  V value
) {
}
