package org.toolkit4j.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CidrTest {

  @Test
  void contains_ipv4NormalizesNetworkAddress() {
    var cidr = Cidr.of("192.168.1.99/24");

    assertEquals("192.168.1.0/24", cidr.toString());
    assertTrue(cidr.contains(Ipv4Address.of("192.168.1.10")));
    assertFalse(cidr.contains(Ipv4Address.of("192.168.2.10")));
  }

  @Test
  void contains_ipv6NormalizesNetworkAddress() {
    var cidr = Cidr.of("2001:db8::1234/64");

    assertEquals("2001:db8:0:0:0:0:0:0/64", cidr.toString());
    assertTrue(cidr.contains(Ipv6Address.of("2001:db8::beef")));
    assertFalse(cidr.contains(Ipv6Address.of("2001:db9::1")));
  }

  @Test
  void contains_rejectsDifferentIpFamilies() {
    assertFalse(Cidr.of("::/0").contains(Ipv4Address.of("127.0.0.1")));
    assertFalse(Cidr.of("0.0.0.0/0").contains(Ipv6Address.of("::1")));
  }
}
