package org.toolkit4j.data.model.money;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyTest {

  private static final Currency USD = Currency.getInstance("USD");
  private static final Currency CNY = Currency.getInstance("CNY");

  @Test
  void equality_normalizesTrailingZeros() {
    var left = Money.of(new BigDecimal("10.0"), USD);
    var right = Money.of(new BigDecimal("10.00"), USD);

    assertEquals(left, right);
  }

  @Test
  void arithmetic_requiresSameCurrency() {
    var amount = Money.of(new BigDecimal("10.50"), USD)
      .add(Money.of(new BigDecimal("2.25"), USD))
      .subtract(Money.of(new BigDecimal("1.00"), USD));

    assertEquals(Money.of(new BigDecimal("11.75"), USD), amount);
  }

  @Test
  void compareTo_rejectsCurrencyMismatch() {
    var usd = Money.of(new BigDecimal("1.00"), USD);
    var cny = Money.of(new BigDecimal("1.00"), CNY);

    assertThrows(IllegalArgumentException.class, () -> usd.compareTo(cny));
  }

  @Test
  void signHelpers_reflectAmountSign() {
    var positive = Money.of(new BigDecimal("5"), USD);
    var negative = Money.of(new BigDecimal("-5"), USD);
    var zero = Money.zero(USD);

    assertTrue(positive.isPositive());
    assertTrue(negative.isNegative());
    assertTrue(zero.isZero());
    assertEquals(positive, negative.abs());
  }

  @Test
  void withAmountAndMultiply_preserveCurrency() {
    var base = Money.of(new BigDecimal("2.50"), USD);

    assertEquals("USD", base.currencyCode());
    assertEquals(Money.of(new BigDecimal("7.50"), USD), base.multiply(new BigDecimal("3")));
    assertEquals(Money.of(new BigDecimal("9.99"), USD), base.withAmount(new BigDecimal("9.99")));
  }
}
