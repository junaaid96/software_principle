# 04 · GRASP: assigning responsibilities

**Code:** [`Grasp.java`](../../src/main/java/dev/junaidul/principles/grasp/Grasp.java) · **Tests:** [`GraspTest`](../../src/test/java/dev/junaidul/principles/grasp/GraspTest.java)

GRASP (Craig Larman, *Applying UML and Patterns*) answers the core OOD question: **which class should do this?**

| Pattern | Question it answers | Example (point of sale) |
|---|---|---|
| Information Expert | Who has the data to do it? | `SaleLine.subtotal()`, `Sale.total()` |
| Creator | Who creates an object? The one that contains/aggregates it. | `Sale.addLine` creates `SaleLine` |
| Controller | Who first handles a system event from the UI? | `Register.enterItem()` |
| Low Coupling | Minimise dependencies between classes. | UI only knows `Register` |
| High Cohesion | Keep each class focused. | `Sale` doesn't print or save |
| Polymorphism | Handle variation by type without conditionals. | `TaxPolicy` implementations |
| Pure Fabrication | Invent a non-domain class to keep cohesion. | `ReceiptPrinter` |
| Indirection | Add an intermediary to decouple. | `Register` between UI and domain |
| Protected Variations | Wrap points of likely change behind a stable interface. | `TaxPolicy` |

## How GRASP relates to SOLID
Information Expert + High Cohesion ≈ SRP; Protected Variations ≈ OCP; Low Coupling + Indirection ≈ DIP.

## Interview questions
1. Why is a "Controller" in GRASP not the same as an MVC controller?
2. Give an example of Pure Fabrication from frameworks you use (hint: repositories, services).
3. When does Information Expert lead to poor cohesion?
