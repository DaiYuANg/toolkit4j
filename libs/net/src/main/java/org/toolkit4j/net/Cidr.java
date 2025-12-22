package org.toolkit4j.net;

import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Objects;

/**
 * CIDR 网段（支持 IPv4 / IPv6）
 */
public final class Cidr {
  private final IpAddress network;
  @Getter
  private final int prefixLength;
  private final BigInteger networkValue;
  private final BigInteger maskValue;

  private Cidr(IpAddress network, int prefixLength) {
    if (prefixLength < 0 || (network instanceof Ipv4Address ipv4 && prefixLength > 32)
      || (network instanceof Ipv6Address ipv6 && prefixLength > 128)) {
      throw new IllegalArgumentException("Invalid prefix length: " + prefixLength);
    }
    this.network = network;
    this.prefixLength = prefixLength;

    byte[] bytes = network.bytes();
    this.networkValue = new BigInteger(1, bytes);

    int bits = bytes.length * 8;
    this.maskValue = prefixLength == 0 ? BigInteger.ZERO : BigInteger.ONE.shiftLeft(bits).subtract(BigInteger.ONE)
      .shiftRight(prefixLength).not().and(BigInteger.ONE.shiftLeft(bits).subtract(BigInteger.ONE));
  }

  @Contract("_ -> new")
  public static @NotNull Cidr of(String cidrStr) {
    Objects.requireNonNull(cidrStr, "CIDR cannot be null");
    val parts = cidrStr.split("/", 2);
    if (parts.length != 2) throw new IllegalArgumentException("Invalid CIDR: " + cidrStr);

    IpAddress ip;
    try {
      ip = Ipv4Address.of(parts[0]);
    } catch (IllegalArgumentException e1) {
      ip = Ipv6Address.of(parts[0]);
    }

    int prefix = Integer.parseInt(parts[1]);
    return new Cidr(ip, prefix);
  }

  public boolean contains(@NotNull IpAddress ip) {
    val ipVal = new BigInteger(1, ip.bytes());
    return ipVal.and(maskValue).equals(networkValue);
  }

  @Override
  public String toString() {
    return network + "/" + prefixLength;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof Cidr other &&
      network.equals(other.network) &&
      prefixLength == other.prefixLength;
  }

  @Override
  public int hashCode() {
    return Objects.hash(network, prefixLength);
  }
}
