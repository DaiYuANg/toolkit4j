# Data Model

Artifact: `io.github.daiyuang:data-model:0.0.1`

## What it provides

- Pagination request/response models: `PageRequest`, `PageResult`
- Generic value models: `KeyValue`, `Option`
- Geo model: `Geo`
- Sorting contract: `Sortable`

## Minimal examples

```java
import org.toolkit4j.data.model.PageRequest;
import org.toolkit4j.data.model.PageResult;

var request = new PageRequest();
request.setPage(2);
request.setSize(20);
var offset = request.getPage(); // 20

var result = PageResult.<String>empty();
var normalized = result.normalized();
```

## Notes

- `PageRequest#getPage()` returns offset semantics by design in current API.
- Models are intended for reuse across services and API layers.
