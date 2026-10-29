# 02 · OOP pillars

**Code:** [`OopPillars.java`](../../src/main/java/dev/junaidul/principles/oop/OopPillars.java) · **Tests:** [`OopPillarsTest`](../../src/test/java/dev/junaidul/principles/oop/OopPillarsTest.java)

| Pillar | What it is | In the code |
|---|---|---|
| Encapsulation | Hide state; expose behaviour that keeps invariants true. | `BankAccount` has private balance; `withdraw` refuses overdrafts; `history()` is read-only. |
| Abstraction | Show *what* an object does, hide *how*. | `PaymentMethod.pay()` |
| Inheritance | Reuse and specialise a base type (is-a). | `Card` → `VisaCard` (template method) |
| Polymorphism | One call, behaviour chosen at runtime. | `checkout()` calls `pay()` on cards and wallets alike |

## Intuition
Objects are *responsible actors*, not bags of getters and setters. If outside code computes on an object's data, move that code into the object.

## Modern Java notes
- `record` for immutable data carriers with value equality.
- `sealed` interfaces to model a closed set of subtypes, plus pattern matching `switch`.
- Interfaces with default methods often replace abstract base classes.

## Trade-offs
- Deep inheritance trees are fragile (a change in the base breaks subclasses). Keep them shallow, mark classes `final` by default.
- Anemic domain models (data-only classes plus "service" classes holding all logic) lose the benefits of encapsulation.

## Common mistakes
- Public setters for everything, which makes encapsulation meaningless.
- Returning internal mutable collections.
- Using inheritance for code reuse instead of an is-a relationship.

## Interview questions
1. Difference between abstraction and encapsulation?
2. Compile-time vs runtime polymorphism (overloading vs overriding)?
3. Abstract class vs interface in modern Java?
4. Why is composition often preferred over inheritance?
5. What does `final` on a class/method buy you?
