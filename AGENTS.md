# AGENTS.md

## Project Overview

`toolkit4j` is a lightweight JVM utility toolkit repository.

This repository is **not** a business application, **not** a full-stack framework, and **not** a dependency-heavy integration platform.  
Its purpose is to provide **small, focused, reusable utilities and extensions** for common JVM development scenarios.

Typical examples include:

- collection and data structure utilities
- lightweight data model abstractions
- web/common response models
- Hibernate-related extensions
- ID generation helpers such as Snowflake ID integration
- other small infrastructure-oriented toolkit modules

The project should remain:

- lightweight
- modular
- explicit in semantics
- easy to maintain
- conservative in dependency introduction
- aligned with modern high-version Java practices


---

## Baseline Requirements

### JDK baseline

- The project baseline is **JDK 25**.
- Prefer modern Java language features available in the current baseline.
- Do not write code in an outdated Java style unless compatibility or integration constraints require it.

### Module packaging

- The repository currently does **not** target JPMS support.
- Do not add `module-info.java`, module-path-specific wiring, or JPMS-only documentation unless there is an explicit repository decision to restore it.
- Prefer ordinary classpath-friendly packaging and documentation for published modules.

### Unit testing baseline

- Meaningful code additions and changes must include unit tests unless the change is trivial and obviously safe.
- Public APIs, edge cases, normalization logic, and important utility behavior should be covered by tests.
- Code without tests should not be treated as complete unless the change is trivial.
- Utility code without tests should be treated as incomplete unless there is a strong reason otherwise.

---

## Core Principles

1. **Keep modules focused**
  - Each module should have a clear and narrow responsibility.
  - Do not mix unrelated concerns into the same module.
  - Do not turn a utility module into a mini framework.

2. **Prefer simplicity over abstraction**
  - Avoid unnecessary indirection, generic over-design, or speculative extensibility.
  - Prefer straightforward implementations unless a more abstract design is clearly justified.

3. **JDK first**
  - Always check whether the JDK already provides a clean solution before introducing a third-party library.
  - Prefer JDK standard capabilities over external dependencies whenever practical.

4. **Minimize dependencies**
  - This repository is a utility toolkit, so dependency footprint must remain small.
  - Every third-party dependency must have strong justification.

5. **APIs must be semantically precise**
  - Naming, behavior, and documentation must align.
  - Avoid APIs whose field names, method names, and actual runtime behavior imply different semantics.

6. **Prioritize maintainability**
  - Code should be easy to read, test, debug, and evolve.
  - Avoid clever designs that reduce readability.

7. **Do not introduce business-specific logic**
  - This repository is for generic reusable toolkit capabilities.
  - Do not add company-specific or product-specific business behavior.

8. **Prefer stable, predictable behavior**
  - Utility libraries should optimize for correctness, clarity, and predictability over novelty.


---

## Scope Control

### Suitable additions

The following kinds of contributions are generally appropriate:

- small reusable utility abstractions
- focused data structures and collection helpers
- generic DTO / page / result / key-value style models
- lightweight framework extensions
- Hibernate helpers or ID generator integrations
- isolated infrastructure utility code
- small internal algorithms with clear reuse value

### Unsuitable additions

The following kinds of contributions should generally be avoided:

- business domain logic
- application service logic
- controller/service/repository layered architecture code
- large framework-style runtime containers
- overly generic plugin systems without proven need
- heavy integrations that introduce large dependency trees
- utilities that are actually one-off project code
- code added “just in case” without repository fit


---

## Java Style Preference

This repository should follow a **modern high-version Java style**.

1. Prefer modern Java language features when they improve clarity.
2. Prefer `record` for simple immutable data carriers.
3. Prefer sealed types when they make the model clearer.
4. Prefer explicit and readable APIs over legacy-style boilerplate.
5. Prefer standard JDK APIs before looking for external helpers.
6. Avoid old Java idioms when newer language constructs make intent clearer.
7. Use modern Java features intentionally, not just for novelty.
8. Code should feel natural for **JDK 25**, not legacy Java 8 style.
9. Prefer functional and stream-based style where it improves readability and intent expression.
10. For index-based iteration, prefer `IntStream` over manual `for (int i = ...)` loops when the stream form is clear and does not harm readability.
11. If a local variable can be treated as immutable, prefer declaring it with Lombok `val` where that improves readability.
12. Do not force `Stream`, `IntStream`, or `val` in cases where they make the code harder to read, harder to debug, or more awkward than a straightforward alternative.

### Examples of preferred direction

- `record` over verbose immutable POJOs where appropriate
- standard JDK collections and APIs over unnecessary wrappers
- clear factory methods over overengineered builders when the model is simple
- direct and explicit control flow over excessive abstraction
- stream-based transformations over manual temporary collection building when the stream version is clearer
- `val` for obvious immutable local variables when the inferred type remains clear

### Avoid

- writing modern modules in an unnecessarily old-fashioned style
- introducing compatibility-oriented compromises without actual need
- forcing patterns that exist only because older Java lacked language features
- forcing streams in logic that is clearer with ordinary control flow
- using `val` when it hides important type information

---

## JDK First Policy

Always prefer the JDK standard library first.

Before introducing any external dependency, check whether the requirement can be solved cleanly with:

- JDK collections
- JDK concurrency utilities
- JDK time API
- JDK I/O and NIO
- JDK reflection / annotation support
- JDK networking support
- JDK functional interfaces
- JDK math / utility APIs
- other standard JDK capabilities

Do not introduce a third-party dependency when the JDK already provides a clean and maintainable solution.

A third-party dependency is justified only when it provides substantial value beyond what the JDK can reasonably support.


---

## Dependency Policy

Dependency introduction must be conservative.

### Rules for third-party libraries

1. **Do not introduce a third-party dependency casually.**
2. A new dependency must provide significant value relative to its cost.
3. Evaluate:
  - maintenance activity
  - ecosystem adoption
  - compatibility with JDK 25 baseline
  - transitive dependency weight
  - API stability
  - learning and maintenance cost
  - packaging simplicity and integration cost
4. Prefer libraries that are:
  - actively maintained
  - widely adopted
  - small in footprint
  - focused in purpose
  - stable in API design

### Small-feature rule

If a third-party library is only needed for a **very small subset of functionality**, then:

- **do not introduce the dependency by default**
- first evaluate whether that functionality can be implemented internally with low complexity and low maintenance risk

This repository should prefer **small self-contained implementations** over pulling in a large dependency for one or two helper methods.

### When internal implementation is preferred

Prefer implementing functionality in-repo when:

- the required behavior is small and well understood
- the implementation is short, stable, and easy to test
- introducing the dependency would be heavier than the value it brings
- only a tiny fraction of the dependency would actually be used

### When third-party dependency is acceptable

A dependency may be introduced when:

- the problem is non-trivial and easy to get wrong
- correctness, compatibility, or security is hard to reproduce internally
- the library solves a substantial problem, not a tiny helper problem
- the dependency aligns with the module purpose
- there is no lightweight and maintainable internal alternative

### Avoid

- utility mega-libraries for isolated helper usage
- dependencies with unclear maintenance status
- dependencies with large transitive trees unless truly justified
- overlapping dependencies that solve the same problem


---

## Lombok Policy

Lombok is preferred in this repository where it improves clarity and reduces boilerplate.

### Preferred Lombok usage

Use Lombok for:

- getters/setters
- constructors
- builder patterns when appropriate
- `@EqualsAndHashCode`
- `@ToString`
- `@UtilityClass`
- logging helpers where applicable

### Lombok usage rules

1. Use Lombok to reduce boilerplate, not to hide poor design.
2. Do not use Lombok annotations that make behavior unclear to readers.
3. Prefer explicit code when Lombok would reduce readability or obscure semantics.
4. For public APIs and important model objects, ensure the generated shape remains obvious.
5. Avoid annotation combinations that make lifecycle and mutability hard to understand.

### Special note

If a Java `record` is clearly a better fit than a Lombok-decorated class, prefer `record`.  
Do not force Lombok where native Java language constructs are more suitable.


---

## Utility Class Restrictions

Do not create broad miscellaneous utility classes casually.

Avoid classes such as:

- `CommonUtils`
- `GenericUtils`
- `ToolkitUtils`
- `HelperUtils`
- `MiscUtils`

Also avoid dumping unrelated methods into broad utility holders such as:

- `StringUtils` used as a catch-all dumping ground
- `CollectionUtils` with unrelated convenience methods
- `ObjectUtils` with mixed semantics

If functionality has a real domain, place it in a properly named and scoped type.

Only create utility classes when:

- the responsibility is narrow and explicit
- the methods are strongly related
- the naming reflects real cohesion
- a dedicated type is clearer than spreading static helpers elsewhere

Prefer domain-specific naming and module-local cohesion over giant convenience classes.


---

## Module Design Rules

1. Modules must have a **single primary purpose**.
2. Cross-module dependencies should be minimized.
3. Lower-level utility modules must not depend on higher-level integration modules.
4. Avoid circular dependency design in any form.
5. If a feature can exist in an existing module without violating module responsibility, prefer reusing that module.
6. If a feature introduces a distinct responsibility, create a new module only when the separation is justified.
7. Before creating a new module, ask:
  - Is this capability reusable?
  - Is this concern independent enough?
  - Does it justify separate publishing/versioning in the future?
  - Is the module boundary and dependency direction still clear without extra packaging complexity?

### Preferred direction

- generic model code stays in model-oriented modules
- framework-specific integration stays in dedicated integration modules
- collection/data structure code stays isolated from framework integration
- framework integration modules should depend on core utilities, not the reverse


---

## API Design Rules

1. Public APIs must be minimal and intentional.
2. Avoid exposing internal implementation details.
3. Method names, field names, and return semantics must match.
4. Avoid ambiguous names such as values that are actually offsets, indexes, or internal states unless named accordingly.
5. Prefer immutable models when practical.
6. Use null conservatively; prefer explicit defaults or well-defined nullable behavior.
7. If normalization/defaulting behavior exists, make it explicit and predictable.
8. Avoid surprising side effects in utility methods.

### Backward compatibility

For published or reusable APIs:

- avoid unnecessary API churn
- avoid renaming public APIs without strong justification
- prefer additive evolution over disruptive redesign
- if an existing API is semantically flawed, fix it carefully and document the reason


---

## Code Style Rules

1. Prefer readability over brevity.
2. Keep classes and methods small and focused.
3. Avoid deeply nested logic where a simpler structure is possible.
4. Avoid speculative abstractions.
5. Avoid magic values; use named constants where meaningful.
6. Write code that is easy to debug.
7. Comments should explain **why**, not restate **what** the code already says.
8. Keep package structure aligned with module responsibility.

### Naming

- Names must be clear and domain-appropriate.
- Avoid vague names like `Utils`, `Helper`, `Manager`, `Processor` unless the abstraction is truly justified.
- Prefer precise names reflecting actual responsibility.

### Exception handling

- Do not swallow exceptions silently.
- Use checked/unchecked exceptions intentionally.
- Exception messages should help diagnosis.

### Logging

- Logging should be minimal and useful.
- Do not add noisy logs in low-level utility code unless operationally necessary.


---

## Testing Rules

All meaningful additions should include tests unless the change is trivial and obviously safe.

### Test expectations

1. Add unit tests for public behavior.
2. Test edge cases, not only happy paths.
3. Test semantic correctness, not implementation details.
4. Keep tests readable and deterministic.
5. Avoid fragile tests coupled to internals.
6. For data structures or algorithmic utilities, add edge-case coverage.
7. For framework integrations, test actual integration behavior where practical.
8. Tests should run cleanly on the JDK 25 baseline.

### Performance-sensitive code

If a module is performance-oriented, such as collection/data-structure utilities:

- avoid regressions in allocation behavior and algorithmic complexity
- add benchmarks when performance characteristics are part of the value proposition
- do not optimize blindly without evidence


---

## Documentation Rules

When adding or modifying a module:

1. Keep README and module documentation aligned with real behavior.
2. Document module purpose clearly.
3. Provide minimal usage examples for non-obvious APIs.
4. If a feature has constraints or trade-offs, document them directly.
5. Do not leave placeholder documentation in published modules.
6. Document packaging or runtime constraints when they matter to consumers.

### For public-facing modules, document:

- what the module does
- when to use it
- when not to use it
- main dependencies
- minimal example


---

## Change Guidelines for AI Agents

When making changes in this repository:

1. First understand the module purpose before editing.
2. Do not broaden module scope casually.
3. Prefer small, localized changes over broad refactors unless necessary.
4. If refactoring, preserve semantic clarity.
5. Do not introduce dependency-heavy solutions for lightweight problems.
6. Prefer internal implementation for small isolated needs.
7. Use Lombok where it genuinely improves clarity.
8. Prefer modern JDK 25 style when appropriate.
9. Do not rewrite working code without clear benefit.
10. Keep public APIs stable and explicit.
11. Add or update tests when behavior changes.
12. Keep packaging and consumer setup simple unless stronger structure is clearly justified.
13. Update documentation when public behavior or module purpose changes.

### Before introducing a dependency, explicitly evaluate

- Why is it needed?
- Can the required subset be implemented internally?
- Is the maintenance cost justified?
- Does it fit the lightweight philosophy of this repository?
- Does it fit the repository's lightweight packaging model without adding unnecessary complexity?

### Before adding a new abstraction, explicitly evaluate

- Is this abstraction solving a real repeated problem?
- Does it reduce complexity, or increase it?
- Is it appropriate for a utility toolkit repository?

### Before creating a new utility class, explicitly evaluate

- Is the responsibility cohesive?
- Is the naming precise?
- Is this becoming a dumping ground?
- Would a domain-specific type be clearer?


---

## Non-Goals

This repository is not intended to become:

- a full application framework
- a business capability platform
- a dependency-heavy convenience wrapper collection
- a dumping ground for unrelated reusable code
- a replacement for established large ecosystems

Keep it focused.  
Keep it lightweight.  
Keep it modern.  
Keep it understandable.  
Keep it useful.
