# Hibernate Snowflake ID

Artifact: `io.github.daiyuang:hibernate-snowflake-id:0.0.1`

## What it provides

- Hibernate ID generator integration based on Agrona `SnowflakeIdGenerator`
- Annotation: `@SnowflakeGenerator`
- Optional node-id configuration key: `snow.flake.generator.node-id`

## Minimal examples

```java
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.toolkit4j.integration.hibernate.snowflake.id.SnowflakeGenerator;

@Entity
class OrderEntity {
  @Id
  @SnowflakeGenerator
  private Long id;
}
```

Set explicit node id in generator parameters/properties with key:

`snow.flake.generator.node-id`

## Notes

- Current implementation validates node id against default Agrona layout bounds.
