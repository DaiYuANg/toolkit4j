package org.toolkit4j.hibernate.snowflake.id;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Properties;
import org.hibernate.MappingException;
import org.junit.jupiter.api.Test;

class HibernateSnowflakeIdGeneratorTest {

  @Test
  void configure_acceptsBoundaryNodeIds() {
    var gen = new HibernateSnowflakeIdGenerator();
    for (String v : new String[] {"0", " 42 ", "1023"}) {
      var p = new Properties();
      p.setProperty(HibernateConfigureKey.SNOW_FLAKE_HIBERNATE_PROPERTIES_KEY, v);
      gen.configure(null, p);
      assertInstanceOf(Long.class, gen.generate(null, null));
    }
  }

  @Test
  void configure_rejectsNodeIdAboveAgronaDefaultMax() {
    var gen = new HibernateSnowflakeIdGenerator();
    var p = new Properties();
    p.setProperty(HibernateConfigureKey.SNOW_FLAKE_HIBERNATE_PROPERTIES_KEY, "1024");
    assertThrows(MappingException.class, () -> gen.configure(null, p));
  }

  @Test
  void configure_rejectsNegativeNodeId() {
    var gen = new HibernateSnowflakeIdGenerator();
    var p = new Properties();
    p.setProperty(HibernateConfigureKey.SNOW_FLAKE_HIBERNATE_PROPERTIES_KEY, "-1");
    assertThrows(MappingException.class, () -> gen.configure(null, p));
  }

  @Test
  void configure_rejectsNonNumericNodeId() {
    var gen = new HibernateSnowflakeIdGenerator();
    var p = new Properties();
    p.setProperty(HibernateConfigureKey.SNOW_FLAKE_HIBERNATE_PROPERTIES_KEY, "not-a-number");
    assertThrows(MappingException.class, () -> gen.configure(null, p));
  }

  @Test
  void generate_producesDistinctIds() {
    var gen = new HibernateSnowflakeIdGenerator();
    var p = new Properties();
    p.setProperty(HibernateConfigureKey.SNOW_FLAKE_HIBERNATE_PROPERTIES_KEY, "1");
    gen.configure(null, p);
    long a = (Long) gen.generate(null, null);
    long b = (Long) gen.generate(null, null);
    assertTrue(b > a, "snowflake ids should increase over time on same node");
  }
}
