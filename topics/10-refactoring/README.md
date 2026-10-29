# 10 · Refactoring and code smells

**Code:** [`Refactoring.java`](../../src/main/java/dev/junaidul/principles/refactoring/Refactoring.java) · **Tests:** [`RefactoringTest`](../../src/test/java/dev/junaidul/principles/refactoring/RefactoringTest.java) (characterization test proves behaviour is unchanged)

> Refactoring (Martin Fowler): a change to the internal structure of software to make it easier to understand and cheaper to modify **without changing its observable behaviour**.

## The safe loop
1. Make sure tests cover the code (write characterization tests if not).
2. Take a tiny step (one refactoring).
3. Run tests. Commit.
4. Repeat. Never mix refactoring and feature changes in one commit.

## Smell → refactoring catalog
| Smell | Symptom | Refactoring |
|---|---|---|
| Long Method | scrolls; needs comments to explain sections | Extract Method |
| Large Class / God Class | many unrelated fields | Extract Class |
| Long Parameter List | `f(a, b, c, d, e)` | Introduce Parameter Object |
| Primitive Obsession | `int type`, `String email` everywhere | Replace with Value Object / Enum |
| Switch Statements on type | same `switch(type)` in many places | Replace Conditional with Polymorphism |
| Duplicate Code | copy-paste | Extract Method / Pull Up Method |
| Feature Envy | method uses another class's data more than its own | Move Method |
| Data Clumps | same 3 fields always together | Extract Class |
| Shotgun Surgery | one change → edits in many classes | Move Method/Field, Inline Class |
| Divergent Change | one class changes for many reasons | Extract Class (SRP) |
| Message Chains | `a.b().c().d()` | Hide Delegate |
| Speculative Generality | unused hooks "for later" | Collapse Hierarchy, Inline |
| Magic Numbers | `* 0.9` | Replace Magic Number with Constant |
| Dead Code | never called | Delete it |

## Before / after in this repo
`before(int type, int days)` has a type-code switch, duplicated "base + extra days" logic and magic numbers. `after()` uses an enum with polymorphic `price()`, an extracted `tiered()` helper, a named constant and a `Rental` parameter object. The parameterized test runs both on the same inputs.

## Interview questions
1. How do you refactor legacy code with no tests?
2. When should you *not* refactor?
3. Explain the "Strangler Fig" approach for large rewrites.
