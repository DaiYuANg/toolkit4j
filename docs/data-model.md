# Data Model

Artifact: `io.github.daiyuang:data-model:0.0.3`

## What it provides

- `page`: `PageRequest`, `PageResult`
- `envelope`: `Result<C, T>`
- `sort`: `Sortable`
- `range`: `Range<T>`, `Bound<T>`, `BoundType`
- `money`: `Money`
- `enumeration`: `EnumValue<T>`, `EnumLookup`, `EnumValues`
- `error`: `ErrorCode`, `ErrorInfo`, `FieldError`
- `value`: `KeyValue<K, V>`, `Option<T>`, `Geo`

## Minimal examples

```java
import org.toolkit4j.data.model.page.PageRequest;
import org.toolkit4j.data.model.page.PageResult;
import org.toolkit4j.data.model.envelope.Result;
import org.toolkit4j.data.model.money.Money;
import org.toolkit4j.data.model.range.Range;
import org.toolkit4j.data.model.sort.Sortable;

var request = new PageRequest();
request.setPage(2);
request.setSize(20);
var page = request.getPage();   // 2
var offset = request.getOffset(); // 20

var result = PageResult.<String>empty();
var normalized = result.normalized();

var ok = Result.of(0, "ok", java.util.Map.of("id", 1L));
var noData = ok.withoutData();

var total = Money.of(new java.math.BigDecimal("19.99"), java.util.Currency.getInstance("USD"));
var activeWindow = Range.closed(1, 10);

record Step(String name, int order) implements Sortable {
  @Override public int getOrder() { return order; }
}
var orderedSteps = Sortable.sort(new java.util.ArrayList<>(java.util.List.of(
  new Step("second", 20),
  new Step("first", 10)
)));
```

## Enumeration

```java
import org.toolkit4j.data.model.enumeration.EnumValue;
import org.toolkit4j.data.model.enumeration.EnumValues;

enum Status implements EnumValue<String> {
  ENABLED("enabled"),
  DISABLED("disabled");

  private final String primaryValue;

  Status(String primaryValue) {
    this.primaryValue = primaryValue;
  }

  @Override
  public String getPrimaryValue() {
    return primaryValue;
  }
}

var lookup = EnumValues.lookup(Status.class);
var enabled = lookup.fromPrimaryValue("enabled");
var optional = lookup.findByPrimaryValue("missing");
```

## Money

```java
import org.toolkit4j.data.model.money.Money;

var usd = java.util.Currency.getInstance("USD");
var subtotal = Money.of(new java.math.BigDecimal("19.99"), usd);
var tax = Money.of(new java.math.BigDecimal("1.50"), usd);
var total = subtotal.add(tax);
var doubled = subtotal.multiply(new java.math.BigDecimal("2"));
```

## Range

```java
import org.toolkit4j.data.model.range.Range;

var closed = Range.closed(1, 10);
var open = Range.open(1, 10);
var anyPositive = Range.greaterThan(0);

var includesTen = closed.contains(10);  // true
var includesOne = open.contains(1);     // false
```

## Error

```java
import org.toolkit4j.data.model.error.ErrorInfo;
import org.toolkit4j.data.model.error.FieldError;

var error = ErrorInfo.of("INVALID_INPUT", "invalid input")
  .withDetail(new FieldError("name", "blank", "must not be blank", null));
```

## Notes

- Integration-facing models such as `page`, `envelope`, and `error` prefer extensible `class` types with Lombok-generated accessors.
- Closed, immutable value objects such as `money`, `range`, and small `value` carriers prefer `record`.
- `PageRequest#getPage()` returns the page number stored in the DTO.
- `PageRequest#getOffset()` provides the database offset derived from page and size.
- `PageResult.empty()` uses first-page semantics (`page = 1`) rather than zero-based page numbering.
- `Result<C, T>` now lives under `org.toolkit4j.data.model.envelope`.
- `Money` is intentionally lightweight: same-currency arithmetic only, with no exchange-rate or formatting layer.
- `data-model` is organized by subpackage responsibility rather than a flat package.
- Models are intended for reuse across services and API layers.
