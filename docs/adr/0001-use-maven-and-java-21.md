# ADR 0001: Use Maven, Java 21 and JUnit 5 for all examples

- **Status:** Accepted
- **Date:** 2026-10-30

## Context
The original repository held five loose `.java` files with no build tool or tests. Learners need to run every example and see it verified, and the examples should use modern Java features (records, sealed types, pattern matching, virtual threads).

## Decision
We will use a single Maven module with the Maven Wrapper, target Java 21 (the LTS baseline; newer JDKs also work) and test every runnable example with JUnit 5. CI runs `./mvnw verify` on every push and pull request.

## Options considered
1. **Maven single module** (chosen): ubiquitous, zero-config for IDEs, one command to verify everything.
2. Gradle: flexible but adds a Kotlin/Groovy DSL to learn.
3. Multi-module per topic: stronger isolation but more boilerplate than a learning repo needs.

## Consequences
- Anyone with a JDK 21+ runs `./mvnw verify`; no Maven install needed.
- Topics share one package tree (`dev.junaidul.principles.<topic>`); guides live in `topics/`.
- Concept-only topics (security, system design, etc.) have README guides without code.
