# Web Data Model

Artifact: `io.github.daiyuang:web-data-model:0.0.1`

## What it provides

- Generic response wrapper: `Result<C, T>`
- Factory methods for common response construction patterns

## Minimal examples

```java
import org.toolkit4j.web.data.model.Result;

var ok = Result.of(0, "ok", java.util.Map.of("id", 1L));
var onlyMessage = Result.ofMessage("created");
var noData = ok.withoutData();
```

## Notes

- `C` and `T` are fully generic so you can align with your own status code and payload conventions.
