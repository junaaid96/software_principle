# 23 · Software engineer checklist (cheat sheet)

Use before opening a PR, during design reviews, and before release.

## Design
- [ ] Each class/module has one reason to change (SRP); names match the domain language.
- [ ] Dependencies point inward toward business rules (DIP / hexagonal).
- [ ] Variation points sit behind interfaces only where change is real (OCP vs YAGNI).
- [ ] Composition used instead of inheritance for reuse; subclasses honour contracts (LSP).
- [ ] Aggregates guard invariants; value objects are immutable.
- [ ] A decision that's expensive to change has an ADR.

## Code
- [ ] Intention-revealing names; small functions; no magic numbers.
- [ ] Inputs validated at the boundary (fail fast); no `null` returns for errors.
- [ ] Exceptions are specific, keep their cause, and are never swallowed.
- [ ] No duplicated knowledge (DRY); no speculative code (YAGNI).
- [ ] Shared mutable state avoided or guarded; remote calls have timeouts.
- [ ] Left the code cleaner than I found it (Boy Scout).

## Tests
- [ ] Unit tests for new behaviour, written first or alongside.
- [ ] Edge cases: empty, null, max, duplicates, concurrency, time zones.
- [ ] Tests are fast, deterministic and independent (inject clocks, seed randomness).
- [ ] Integration tests for DB/HTTP boundaries.

## API
- [ ] Correct verbs and status codes; RFC 9457 errors; pagination capped.
- [ ] POSTs that cause side effects accept an idempotency key.
- [ ] Backward compatible, or versioned with a deprecation plan.

## Security
- [ ] Authorization checked per resource; least privilege.
- [ ] Parameterised queries; output encoded.
- [ ] No secrets in code, logs or git; dependencies scanned.

## Operability
- [ ] Structured logs with correlation id; no personal data in logs.
- [ ] Metrics (RED) and traces for the new path; alerts tied to SLOs.
- [ ] Feature flag or rollback plan; DB migrations are backward compatible.

## Process
- [ ] Small PR with a clear description and green CI.
- [ ] Conventional commit messages.
- [ ] Docs, README and CHANGELOG updated.
