# 16 · Observability

Being able to answer "what is the system doing and why?" from its outputs, without shipping new code.

## Three pillars (+1)
| Signal | Answers | Tools |
|---|---|---|
| **Logs** | what happened in this request? | structured JSON logs, SLF4J + Logback, Loki/ELK |
| **Metrics** | how much / how fast, over time? | Micrometer, Prometheus, Grafana |
| **Traces** | where did time go across services? | OpenTelemetry, Jaeger, Tempo |
| Profiles | which code burns CPU/memory? | continuous profiling (Pyroscope, JFR) |

Use **OpenTelemetry** as the vendor-neutral standard, and propagate a trace/correlation id through every hop.

## Logging practices
- Structured key-values (`orderId=42 status=PAID`), not prose.
- Correct levels: ERROR (needs a human), WARN, INFO (business events), DEBUG.
- Never log secrets, tokens or personal data.
- One log line per event; no logging *and* rethrowing the same exception at every layer.

## What to measure
- **RED** for services: Rate, Errors, Duration.
- **USE** for resources: Utilisation, Saturation, Errors.
- **Golden signals:** latency, traffic, errors, saturation.

## SLI → SLO → error budget
- SLI: measured indicator (e.g. % of requests < 300 ms).
- SLO: target (99.9% over 30 days).
- Error budget: allowed failure (0.1%). When it's spent, prioritise reliability over features.

## Alerting
Alert on user-visible symptoms (SLO burn rate), not on every CPU spike. Every alert needs a runbook.

## Interview questions
1. Monitoring vs observability?
2. How would you debug a latency spike across five microservices?
