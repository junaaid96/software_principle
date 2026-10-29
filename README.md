# software_principle

[![CI](https://github.com/junaaid96/software_principle/actions/workflows/ci.yml/badge.svg)](https://github.com/junaaid96/software_principle/actions/workflows/ci.yml)
![Java 21+](https://img.shields.io/badge/Java-21%2B-orange)
![License: MIT](https://img.shields.io/badge/License-MIT-blue)

A hands-on curriculum for **software engineering principles, object-oriented design and engineering best practices**, with runnable, line-commented **Java 21** examples and **JUnit 5** tests. This repo started as `SOLID_principle`, and SOLID is still the first chapter.

Every topic has a concept guide covering what it is, why it matters, when to use it, intuition, trade-offs, common mistakes and interview questions. Code topics show a **BAD** version next to a **GOOD** one, and tests prove the behaviour.

## Website
📖 Read the guides online at **https://junaaid96.github.io/software_principle/**, with search, dark mode, rendered diagrams and the Java sources inline. The site is built from this repo's Markdown (see [`website/`](website)).

## Quick start
```bash
git clone https://github.com/junaaid96/software_principle.git
cd software_principle
./mvnw verify            # compiles everything and runs all tests (JDK 21+ required)
java -cp target/classes dev.junaidul.principles.solid.Lsp   # run any example's main()
```

## Learning path
Suggested order: fundamentals → design → architecture → craft → delivery. A visual map is in [`docs/diagrams/repo-map.md`](docs/diagrams/repo-map.md).

| # | Topic | Guide | Code |
|---|---|---|---|
| **Fundamentals** ||||
| 01 | SOLID principles (bad vs good) | [guide](topics/01-solid) | [`solid/`](src/main/java/dev/junaidul/principles/solid) |
| 02 | OOP pillars | [guide](topics/02-oop) | [`oop/`](src/main/java/dev/junaidul/principles/oop) |
| 03 | DRY, KISS, YAGNI, Law of Demeter, SoC, Composition over Inheritance, Tell Don't Ask, Least Astonishment, Fail Fast, Boy Scout, CQS | [guide](topics/03-core-principles) | [`core/`](src/main/java/dev/junaidul/principles/core) |
| 04 | GRASP responsibility patterns | [guide](topics/04-grasp) | [`grasp/`](src/main/java/dev/junaidul/principles/grasp) |
| **Design** ||||
| 05 | OOAD + UML: requirements → use cases → domain model → CRC → diagrams → code (library case study) | [guide](topics/05-ooad) | [`ooad/library/`](src/main/java/dev/junaidul/principles/ooad/library) |
| 06 | Gang of Four design patterns (16 most-used) | [guide](topics/06-design-patterns) | [`patterns/`](src/main/java/dev/junaidul/principles/patterns) |
| 07 | Architecture: layered, hexagonal, clean, MVC/MVVM, event-driven, CQRS, monolith vs microservices | [guide](topics/07-architecture) | [`architecture/`](src/main/java/dev/junaidul/principles/architecture) |
| 08 | Domain-Driven Design basics | [guide](topics/08-ddd) | [`ddd/`](src/main/java/dev/junaidul/principles/ddd) |
| **Craft** ||||
| 09 | Clean code | [guide](topics/09-clean-code) | [`cleancode/`](src/main/java/dev/junaidul/principles/cleancode) |
| 10 | Refactoring and code smells | [guide](topics/10-refactoring) | [`refactoring/`](src/main/java/dev/junaidul/principles/refactoring) |
| 11 | Testing: pyramid, TDD, test doubles, BDD, property-based | [guide](topics/11-testing) | [`testing/`](src/main/java/dev/junaidul/principles/testing) |
| 12 | API design: REST, versioning, idempotency, pagination, errors | [guide](topics/12-api-design) | [`api/`](src/main/java/dev/junaidul/principles/api) |
| 13 | Concurrency principles (incl. virtual threads) | [guide](topics/13-concurrency) | [`concurrency/`](src/main/java/dev/junaidul/principles/concurrency) |
| 14 | Security: OWASP Top 10, secure coding | [guide](topics/14-security) | concept guide |
| **Systems and delivery** ||||
| 15 | Performance and scalability | [guide](topics/15-performance-scalability) | concept guide |
| 16 | Observability: logs, metrics, traces, SLOs | [guide](topics/16-observability) | concept guide |
| 17 | CI/CD and DevOps | [guide](topics/17-cicd-devops) | [`ci.yml`](.github/workflows/ci.yml) |
| 18 | Git workflows, conventional commits, code review | [guide](topics/18-version-control-code-review) | concept guide |
| 19 | SDLC and Agile: Scrum, Kanban, XP | [guide](topics/19-sdlc-agile) | concept guide |
| 20 | System design: caching, load balancing, databases, CAP, queues | [guide](topics/20-system-design) | concept guide |
| 21 | Technical debt | [guide](topics/21-technical-debt) | concept guide |
| 22 | Documentation and ADRs | [guide](topics/22-documentation-adrs) | [`docs/adr/`](docs/adr) |
| 23 | **Software engineer checklist** (cheat sheet) | [checklist](topics/23-checklist) | |

## Repository layout
```
software_principle/
├── topics/NN-topic/README.md          # concept guides (start here)
├── src/main/java/dev/junaidul/principles/<topic>/   # runnable, commented examples
├── src/test/java/dev/junaidul/principles/<topic>/   # JUnit 5 tests for each example
├── docs/diagrams/                     # Mermaid diagrams (OOAD case study, learning map)
├── docs/adr/                          # Architecture Decision Records + template
├── .github/workflows/ci.yml           # build + test on every push / PR
├── .github/workflows/pages.yml        # builds and deploys the docs website
├── website/                           # VitePress docs site (generated from the Markdown)
├── CONTRIBUTING.md
└── LICENSE (MIT)
```

## Recommended reading
*Clean Code* and *Clean Architecture* (R. C. Martin) · *Refactoring* (M. Fowler) · *Design Patterns* (Gamma et al.) · *Head First Design Patterns* · *Applying UML and Patterns* (C. Larman) · *Domain-Driven Design* (E. Evans) · *Effective Java* (J. Bloch) · *The Pragmatic Programmer* · *Designing Data-Intensive Applications* (M. Kleppmann) · *Accelerate* (Forsgren et al.)

## Contributing
See [CONTRIBUTING.md](CONTRIBUTING.md). Issues and pull requests are welcome.

## License
[MIT](LICENSE)
