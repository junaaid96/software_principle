# 03 · Core design principles

**Code:** [`CorePrinciples.java`](../../src/main/java/dev/junaidul/principles/core/CorePrinciples.java) · **Tests:** [`CorePrinciplesTest`](../../src/test/java/dev/junaidul/principles/core/CorePrinciplesTest.java)

| Principle | Rule of thumb | Watch out for |
|---|---|---|
| **DRY** (Don't Repeat Yourself) | Every piece of *knowledge* has one authoritative home (`Tax.VAT_RATE`). | Merging code that only *looks* alike but changes for different reasons. Duplication is cheaper than the wrong abstraction. |
| **KISS** (Keep It Simple) | Choose the simplest correct solution. | "Clever" one-liners nobody can review. |
| **YAGNI** (You Aren't Gonna Need It) | Build for today's requirement; refactor when the need is real. | Plugin systems with one plugin. |
| **Separation of Concerns** | UI, business rules and I/O live in different modules. | Business logic in controllers or SQL in views. See `architecture/Hexagonal.java`. |
| **Law of Demeter** | Talk only to your direct collaborators: avoid `a.getB().getC().doIt()`. | Train-wreck call chains. |
| **Tell, Don't Ask** | Tell an object what to do (`customer.pay(20)`); do not pull its data and decide for it. | Getters feeding `if` statements in another class. |
| **Composition over Inheritance** | Build behaviour from parts (`Car` has an `Engine`). | Inheriting from concrete classes. |
| **Command-Query Separation** | A method either changes state (command) or returns data (query), not both. | `pop()`-like methods are accepted exceptions. |
| **Principle of Least Astonishment** | Code behaves the way its name suggests (`sorted()` does not mutate). | Getters with side effects. |
| **Fail Fast** | Reject invalid input at the boundary (`Email` record validates in its constructor). | Swallowing exceptions, returning `null`. |
| **Boy Scout Rule** | Leave code a little cleaner than you found it: rename one variable, extract one method. | Giant "cleanup" PRs mixed with features. |

## When to break them
Principles are heuristics. Performance hot paths, generated code or a deadline can justify exceptions. Document the reason (see ADRs in topic 21).

## Interview questions
1. Give an example where DRY made code worse.
2. How does Law of Demeter relate to coupling?
3. Is `Iterator.next()` a CQS violation? Why is it acceptable?
4. How do YAGNI and OCP pull in different directions, and how do you balance them?
