package org.toolkit4j.data.model;

import io.soabase.recordbuilder.core.RecordBuilder;
import lombok.val;

import static java.util.Optional.ofNullable;

@RecordBuilder
@SuppressWarnings("unused")
public record Geo(
  Double longitude,
  Double latitude
) {

  public boolean effective() {
    val longitV = ofNullable(longitude).map(longitude -> longitude > 0).orElse(false);
    val latV = ofNullable(latitude).map(latitude -> latitude > 0).orElse(false);
    return longitV && latV;
  }
}