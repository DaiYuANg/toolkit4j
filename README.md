# toolkit4j

**English** · A lightweight JVM utility toolkit for collections, data models, and small infrastructure helpers.  
**中文** · 面向 JVM 的轻量级工具库，提供集合、数据模型及小型基础设施扩展。

---

## Installation

`toolkit4j` artifacts are published to Maven Central under `io.github.daiyuang`.

### Gradle (Kotlin DSL)

```kotlin
dependencies {
  implementation("io.github.daiyuang:collection:0.0.3")
  // implementation("io.github.daiyuang:data-model:0.0.3")
  // implementation("io.github.daiyuang:net:0.0.3")
  // implementation("io.github.daiyuang:hibernate-snowflake-id:0.0.3")
  // implementation("io.github.daiyuang:quartz-task:0.0.3")
}
```

### Maven

```xml
<dependency>
  <groupId>io.github.daiyuang</groupId>
  <artifactId>collection</artifactId>
  <version>0.0.3</version>
</dependency>
```

---

## Modules

- `collection`: data structures such as pageable collections, table, trie, and tree helpers.
- `data-model`: reusable model types such as `PageRequest`, `PageResult`, `Result`, `Sortable`, `Range`, `Money`, `ErrorInfo`, `EnumValue`.
- `net`: IP / CIDR utility types (`Ipv4Address`, `Ipv6Address`, `Cidr`, `IpInfo`).
- `hibernate-snowflake-id`: Hibernate integration for Agrona Snowflake ID generator.
- `quartz-task`: high-level Quartz task registration and scheduling API.

---

## Documentation

Start from the docs index:

- [docs/README.md](./docs/README.md)

Per-module guides:

- [Collection](./docs/collection.md)
- [Data Model](./docs/data-model.md)
- [Net](./docs/net.md)
- [Hibernate Snowflake ID](./docs/hibernate-snowflake-id.md)
- [Quartz Task](./docs/quartz-task.md)

---

## License

[Apache License 2.0](./LICENSE.txt)
