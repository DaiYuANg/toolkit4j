package org.toolkit4j.net;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IpAddressTest {

  @Test
  void ipv4RecognizesPrivateAndLoopbackRanges() {
    var loopback = Ipv4Address.of("127.0.0.1");
    var privateAddress = Ipv4Address.of("192.168.1.10");
    var publicAddress = Ipv4Address.of("8.8.8.8");

    assertTrue(loopback.isLoopback());
    assertTrue(privateAddress.isPrivate());
    assertFalse(publicAddress.isPrivate());
  }

  @Test
  void ipv6RecognizesPrivateAndLoopbackRanges() {
    var loopback = Ipv6Address.of("::1");
    var privateAddress = Ipv6Address.of("fd12:3456::1");
    var publicAddress = Ipv6Address.of("2001:4860:4860::8888");

    assertTrue(loopback.isLoopback());
    assertTrue(privateAddress.isPrivate());
    assertFalse(publicAddress.isPrivate());
  }

  @Test
  void ipv4BytesRoundTrip() {
    assertArrayEquals(new byte[] {(byte) 192, (byte) 168, 1, 10}, Ipv4Address.of("192.168.1.10").bytes());
  }
}
