module org.toolkit4j.collection {
  requires lombok;
  requires static org.jetbrains.annotations;

  exports org.toolkit4j.collection.pageable;
  exports org.toolkit4j.collection.table;
  exports org.toolkit4j.collection.tree;
  exports org.toolkit4j.collection.trie;
  exports org.toolkit4j.collection.util;
}
