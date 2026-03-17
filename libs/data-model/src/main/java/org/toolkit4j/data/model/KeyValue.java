package org.toolkit4j.data.model;

import io.soabase.recordbuilder.core.RecordBuilder;

/**
 * 键值对
 */
@RecordBuilder
public record KeyValue(
  String key,

  String value
) {
}