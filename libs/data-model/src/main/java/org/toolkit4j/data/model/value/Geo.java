package org.toolkit4j.data.model.value;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
@SuppressWarnings("unused")
public record Geo(
  Double longitude,
  Double latitude
) {

  public boolean isComplete() {
    return longitude != null && latitude != null;
  }

  public boolean isValid() {
    return isValidLongitude(longitude) && isValidLatitude(latitude);
  }

  private static boolean isValidLongitude(Double longitude) {
    return longitude != null && longitude >= -180.0d && longitude <= 180.0d;
  }

  private static boolean isValidLatitude(Double latitude) {
    return latitude != null && latitude >= -90.0d && latitude <= 90.0d;
  }
}
