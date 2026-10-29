# 15 · Performance and scalability fundamentals

## Rules
1. **Measure first.** Profile (JFR, async-profiler) before optimising; guesses are usually wrong.
2. Optimise the algorithm before the micro-code (Big-O beats tricks).
3. Set targets as percentiles (p95/p99 latency), not averages.
4. Benchmark with JMH, not `System.currentTimeMillis()` loops.

## Latency numbers worth knowing (order of magnitude)
| Operation | Time |
|---|---|
| L1 cache | ~1 ns |
| Main memory | ~100 ns |
| SSD random read | ~100 µs |
| Same-datacenter round trip | ~0.5 ms |
| Cross-continent round trip | ~100+ ms |

## Common wins
- Fix N+1 queries (fetch joins, batching), add the right indexes, paginate.
- Cache hot reads (see topic 20); avoid caching what you can't invalidate.
- Connection pooling (HikariCP), timeouts on every remote call.
- Async/batch work off the request path (queues).
- Reduce payloads (compression, pagination, field selection).

## Scalability
- **Vertical** (bigger machine) is simple but capped; **horizontal** (more machines) needs stateless services.
- Keep services stateless; move session/state to a DB or cache.
- Scale databases with read replicas, partitioning/sharding, and CQRS read models.
- Protect the system: rate limiting, back-pressure, circuit breakers, bulkheads, load shedding.
- Amdahl's law: the serial part limits speed-up.

## Interview questions
1. How do you find why an endpoint got slow?
2. Throughput vs latency?
3. What breaks first when traffic grows 10x?
