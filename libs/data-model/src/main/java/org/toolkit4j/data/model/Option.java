package org.toolkit4j.data.model;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.List;

@RecordBuilder
public record Option<T>(
  T value,

  String label,

  String tag,

  List<Option<T>> children
) {
}