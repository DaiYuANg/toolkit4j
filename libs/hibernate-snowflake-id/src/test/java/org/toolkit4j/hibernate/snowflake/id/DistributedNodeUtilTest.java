package org.toolkit4j.hibernate.snowflake.id;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DistributedNodeUtilTest {

  @Test
  void nodeIdFromHostname_staysWithinRange() {
    for (int i = 0; i < 500; i++) {
      long id = DistributedNodeUtil.nodeIdFromHostname("host-" + i);
      assertTrue(id >= DistributedNodeUtil.NODE_ID_MIN, "id=" + id);
      assertTrue(id <= DistributedNodeUtil.NODE_ID_MAX, "id=" + id);
    }
  }

  @Test
  void nodeIdFromHostname_mapsZeroHashBucketToMin() {
    long id = DistributedNodeUtil.nodeIdFromHostname("");
    assertEquals(DistributedNodeUtil.NODE_ID_MIN, id);
  }
}
