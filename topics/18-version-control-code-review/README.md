# 18 · Version control and code review

## Git workflows
| Workflow | How | Good for |
|---|---|---|
| Trunk-based | short-lived branches (hours to a day), merge to `main` often, feature flags | most teams with CI/CD |
| GitHub Flow | branch → PR → review → merge → deploy | web apps, continuous delivery |
| Git Flow | `develop`, `release/*`, `hotfix/*` | versioned products with scheduled releases (heavier) |

## Conventional Commits
```
<type>(optional scope): <description>

feat(auth): add password reset
fix(api): return 404 for unknown order
docs: explain ADR process
refactor!: rename OrderStatus values   <- "!" marks a breaking change
```
Types: `feat`, `fix`, `docs`, `style`, `refactor`, `perf`, `test`, `build`, `ci`, `chore`. Enables automatic changelogs and semantic versioning (`MAJOR.MINOR.PATCH`).

## Good commits
- One logical change per commit; message says *why*.
- Never commit secrets or build output (`.gitignore`).
- Rebase your own branch to keep history clean; never rewrite shared history.

## Pull requests
- Small (< ~400 lines changed), focused, with a description: what, why, how tested, screenshots.
- Green CI before review. Link the issue.

## Code review: what to look for
1. Correctness and edge cases
2. Design: fits the architecture, right responsibilities
3. Tests: meaningful, cover the change
4. Readability and naming
5. Security and performance
6. Leave style nits to automated formatters

**Tone:** review the code, not the person; ask questions ("what happens if…?"); label nits; approve when it's better than before, not when it's perfect.

## Interview questions
1. Merge vs rebase vs squash?
2. How do you handle a disagreement in code review?
