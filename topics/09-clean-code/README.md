# 09 · Clean code

**Code:** [`CleanCode.java`](../../src/main/java/dev/junaidul/principles/cleancode/CleanCode.java) (bad `calc` vs good `orderTotal`) · **Tests:** [`CleanCodeTest`](../../src/test/java/dev/junaidul/principles/cleancode/CleanCodeTest.java)

Code is read far more often than it is written. Clean code optimises for the reader.

## Naming
- Reveal intent: `orderTotal(prices, tier)` not `calc(l, t)`.
- Classes are nouns (`InvoiceFormatter`), methods are verbs (`format`), booleans read as questions (`isActive`, `hasStock`).
- No encodings or noise words (`strName`, `DataManagerImpl2`). Length should match scope: `i` in a 3-line loop is fine.

## Functions
- Small and doing one thing at one level of abstraction.
- Few parameters (0–3). More → introduce a parameter object.
- No flag arguments (`render(true)`). Split into two functions.
- Guard clauses instead of deep nesting.

## Comments
- Explain **why**, not what. The code says what.
- Delete commented-out code (git remembers it).
- A comment that lies is worse than none (see `// 5% discount` in the bad example).
- Javadoc for public APIs is valuable.

## Formatting
Use an automatic formatter (google-java-format, Spotless, Prettier) and a linter in CI. The team's style beats your personal style.

## Error handling
- Use exceptions, not error codes or `null`. Return `Optional` for "may be absent".
- Specific exceptions with context; keep the cause (`new X(msg, e)`).
- Never swallow exceptions (`catch (Exception e) {}`).
- Validate at boundaries (Fail Fast).

## Other
- Prefer immutability (`final`, records, `List.copyOf`).
- Avoid magic numbers; name constants.
- Keep classes cohesive and small.

## Interview questions
1. What makes a good name?
2. When are comments necessary?
3. Checked vs unchecked exceptions: when do you use each?
