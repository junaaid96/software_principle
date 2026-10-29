# 22 · Documentation and Architecture Decision Records

## Docs that pay off
| Doc | Audience | Contains |
|---|---|---|
| README | newcomers | what it is, how to run, test and deploy |
| CONTRIBUTING | contributors | workflow, style, commit rules |
| API reference (OpenAPI/Javadoc) | integrators | contracts, examples |
| Architecture overview (C4 model) | engineers | context, containers, components |
| Runbooks | on-call | how to diagnose and fix known issues |
| ADRs | future team | why decisions were made |
| CHANGELOG | users | what changed per version |

**Docs as code:** Markdown in the repo, reviewed in PRs, diagrams as text (Mermaid, PlantUML), checked by CI link checkers.

## C4 model
Level 1 Context (system + users + external systems) → Level 2 Containers (apps, DBs) → Level 3 Components → Level 4 Code. Usually levels 1–2 are enough.

## ADRs
An ADR records **one** architecturally significant decision. It is short, numbered, immutable (supersede, don't edit) and lives in `docs/adr/`.

Template (Michael Nygard): see [`docs/adr/0000-template.md`](../../docs/adr/0000-template.md) and this repo's real example [`0001-use-maven-and-java-21.md`](../../docs/adr/0001-use-maven-and-java-21.md).

## Interview questions
1. What deserves an ADR?
2. How do you keep docs from going stale?
