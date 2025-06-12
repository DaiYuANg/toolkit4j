package org.toolkit4j.collection.tree;

import java.util.List;

public interface TreeNode<ID, ParentId> {
  ID getId();

  ParentId getParentId();
}
