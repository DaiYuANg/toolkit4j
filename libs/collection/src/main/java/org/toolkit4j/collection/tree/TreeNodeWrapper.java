package org.toolkit4j.collection.tree;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.List;

@RecordBuilder
public record TreeNodeWrapper<T>(
  T data,
  List<TreeNodeWrapper<T>> children
) {
}