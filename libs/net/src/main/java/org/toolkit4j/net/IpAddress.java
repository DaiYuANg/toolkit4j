package org.toolkit4j.net;

public sealed interface IpAddress permits Ipv4Address, Ipv6Address {
  byte[] bytes();

  boolean isLoopback();

  boolean isPrivate();
}
