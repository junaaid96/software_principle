# Contributing

Thanks for helping improve this learning repo.

## Setup
- JDK 21 or newer. No Maven install needed: use `./mvnw`.
- `./mvnw verify` must pass before you open a pull request.

## Adding or improving a topic
1. Put code in `src/main/java/dev/junaidul/principles/<topic>/` and tests in the matching `src/test/java/...` package.
2. Show a **BAD** and a **GOOD** version when it helps, and comment what each part does and why.
3. Add or update `topics/NN-<topic>/README.md` with: what/why/when, intuition, trade-offs, common mistakes, interview questions, and links to the code.
4. Add the topic to the table in the root `README.md`.
5. Diagrams go in `docs/diagrams/` as Mermaid. Significant repo decisions get an ADR in `docs/adr/`.

## Style
- Modern Java: records, sealed types, `var` where the type is obvious, immutability by default.
- Small classes and functions, intention-revealing names, no magic numbers.
- Tests are fast and deterministic (inject `Clock`, seed `Random`).

## Commits and PRs
- Use [Conventional Commits](https://www.conventionalcommits.org): `feat:`, `fix:`, `docs:`, `refactor:`, `test:`, `build:`, `ci:`.
- Keep pull requests small and focused, with a short description of what and why.
