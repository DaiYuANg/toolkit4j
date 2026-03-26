module org.toolkit4j.data.model {
  requires static java.compiler;
  requires io.soabase.recordbuilder.core;
  requires lombok;
  requires static org.jetbrains.annotations;

  exports org.toolkit4j.data.model.envelope;
  exports org.toolkit4j.data.model.enumeration;
  exports org.toolkit4j.data.model.error;
  exports org.toolkit4j.data.model.money;
  exports org.toolkit4j.data.model.page;
  exports org.toolkit4j.data.model.range;
  exports org.toolkit4j.data.model.sort;
  exports org.toolkit4j.data.model.value;
}
