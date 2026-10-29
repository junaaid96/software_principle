# 21 · Technical debt

Technical debt is the implied future cost of choosing a quicker or easier solution now. Like money debt, it is fine when taken **deliberately** and **paid back**; ruinous when ignored.

## Fowler's debt quadrant
| | Deliberate | Inadvertent |
|---|---|---|
| **Prudent** | "Ship now, refactor next sprint." | "Now we know how we should have built it." |
| **Reckless** | "No time for design." | "What's layering?" |

## Symptoms
Slower delivery, fear of changing code, recurring bugs in the same area, long onboarding, flaky builds, outdated dependencies.

## Managing it
1. **Make it visible:** track debt items in the backlog with cost/impact.
2. **Budget** a steady share of capacity (e.g. 15–20%) instead of "cleanup sprints".
3. **Boy Scout rule** for small items; dedicated work for big ones.
4. Prioritise by **interest**: fix debt in code that changes often (hotspots = churn × complexity).
5. Prevent new debt: code review, CI quality gates, ADRs for shortcuts with an expiry.
6. Keep dependencies current (Dependabot/Renovate) since upgrade debt compounds.

## Talking to stakeholders
Frame debt in business terms: "This module causes 40% of our bugs and adds two days to every feature in it."

## Interview questions
1. How do you convince a PM to prioritise refactoring?
2. Give an example of debt you took on deliberately.
