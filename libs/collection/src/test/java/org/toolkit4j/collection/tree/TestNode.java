package org.toolkit4j.collection.tree;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TestNode {
  Integer id;
  Integer parentId;
  String name;
}
