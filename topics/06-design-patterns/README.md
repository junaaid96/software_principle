# 06 · Gang of Four design patterns

**Code:** [`patterns/`](../../src/main/java/dev/junaidul/principles/patterns) · **Tests:** [`PatternsTest`](../../src/test/java/dev/junaidul/principles/patterns/PatternsTest.java)

A design pattern is a named, reusable solution to a recurring design problem. Learn the *problem* first; the class diagram follows.

## Creational ([`Creational.java`](../../src/main/java/dev/junaidul/principles/patterns/Creational.java))
| Pattern | Use when | Real-world Java |
|---|---|---|
| Builder | many optional params; want immutable result | `HttpRequest.newBuilder()`, Lombok `@Builder` |
| Factory Method | caller shouldn't know the concrete class | `List.of()`, `Executors.newFixedThreadPool()` |
| Abstract Factory | create families of related objects | UI themes, JDBC drivers |
| Singleton | exactly one instance (use enum, or let DI manage it) | Spring singleton beans |
| Prototype | copying is cheaper/clearer than constructing | `record` "withers", `clone()` (avoid) |

## Structural ([`Structural.java`](../../src/main/java/dev/junaidul/principles/patterns/Structural.java))
| Pattern | Use when | Real-world Java |
|---|---|---|
| Adapter | an existing API doesn't fit your interface | `Arrays.asList`, `InputStreamReader` |
| Decorator | add behaviour by wrapping, stackable | `BufferedInputStream`, `Collections.unmodifiableList` |
| Facade | simplify a complex subsystem | `JdbcTemplate`, SLF4J |
| Proxy | control access: cache, lazy load, security, remote | Spring AOP `@Transactional`, Hibernate lazy entities |
| Composite | tree of parts treated uniformly | file systems, UI component trees |

## Behavioral ([`Behavioral.java`](../../src/main/java/dev/junaidul/principles/patterns/Behavioral.java))
| Pattern | Use when | Real-world Java |
|---|---|---|
| Strategy | swap algorithms at runtime | `Comparator`, lambdas |
| Observer | many listeners react to events | Spring `ApplicationEvent`, reactive streams |
| Command | actions as objects: undo, queue, log | `Runnable`, job queues |
| Template Method | fixed algorithm, variable steps | `AbstractList`, `HttpServlet.service` |
| State | behaviour depends on state; avoid giant switch | order lifecycle, TCP connection |
| Chain of Responsibility | pass a request along handlers | servlet filters, Spring Security filter chain |

## Trade-offs and mistakes
- Patterns add indirection. Use one when the problem it solves is present, not to look "enterprise".
- In modern Java many patterns shrink to a lambda (Strategy, Command, Observer).
- Singleton = global state; it hurts testing. Prefer DI.

## Interview questions
1. Decorator vs Proxy: same structure, different intent. Explain.
2. Strategy vs State?
3. How would you implement a thread-safe singleton? Why is the enum approach preferred?
4. Which patterns does Spring use internally?
