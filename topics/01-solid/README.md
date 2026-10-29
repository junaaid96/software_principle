# 01 · SOLID principles

**Code:** [`solid/`](../../src/main/java/dev/junaidul/principles/solid) · **Tests:** [`SolidTest`](../../src/test/java/dev/junaidul/principles/solid/SolidTest.java)

Five principles, collected by Robert C. Martin, that keep object-oriented code easy to change. Each example file has a **BAD** version and a **GOOD** version side by side.

| Principle | One-line rule | Example file |
|---|---|---|
| **S**ingle Responsibility | A module has one reason to change (one actor). | `Srp.java`: invoice data vs formatting vs storage |
| **O**pen/Closed | Extend behaviour by adding code, not editing tested code. | `Ocp.java`: `Shape` interface instead of an if-chain |
| **L**iskov Substitution | Subtypes must honour the base type's contract. | `Lsp.java`: Square/Rectangle trap, Bird/Penguin fix |
| **I**nterface Segregation | Many small role interfaces beat one fat one. | `Isp.java`: `Printer` + `Scanner` |
| **D**ependency Inversion | Policy depends on abstractions; details plug in. | `Dip.java`: `SignupService` + `UserStore` |

## Why it matters
- Changes stay local: a new tax rule touches one class, not ten.
- Code becomes testable: DIP lets you pass an in-memory fake instead of a database.
- Merge conflicts drop because unrelated features live in different files.

## Intuition
- **SRP:** ask "who would ask me to change this?" If the answer is "finance *and* the DBA", split it.
- **OCP:** find the axis of change (new shapes, new payment types) and put an interface on it.
- **LSP:** if a subclass throws `UnsupportedOperationException` or needs an `instanceof` check by callers, the hierarchy is wrong.
- **ISP:** clients define the interface they need, not the implementer.
- **DIP:** the *high-level module owns the interface*. That is the "inversion".

## Trade-offs
- Too many tiny classes and interfaces is also a smell ("interface per class"). Apply OCP where change actually happens (YAGNI).
- SRP is about reasons to change, not "one method per class".

## Common mistakes
- Treating DIP as "use a DI framework". Constructor injection with plain `new` at the edge is enough.
- Creating an interface with exactly one implementation "for the future".
- Inheriting just to reuse code, which breaks LSP. Prefer composition.

## Interview questions
1. Give a real example of an SRP violation you fixed.
2. Why does `Square extends Rectangle` violate LSP even though a square *is a* rectangle in maths?
3. How does OCP relate to the Strategy pattern?
4. What is the difference between Dependency Inversion, Dependency Injection and Inversion of Control?
5. When would you deliberately *not* apply ISP?

Run any example: `./mvnw -q compile && java -cp target/classes dev.junaidul.principles.solid.Lsp`
