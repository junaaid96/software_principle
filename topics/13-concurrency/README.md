# 13 · Concurrency principles

**Code:** [`Concurrency.java`](../../src/main/java/dev/junaidul/principles/concurrency/Concurrency.java) · **Tests:** [`ConcurrencyTest`](../../src/test/java/dev/junaidul/principles/concurrency/ConcurrencyTest.java)

## Core ideas
- **Race condition:** result depends on timing. `count++` is read-modify-write, so updates get lost (`UnsafeCounter`).
- **Visibility:** without synchronization, one thread may never see another's write (`volatile`, locks and atomics create *happens-before*).
- **Atomicity:** use `AtomicInteger`, `LongAdder`, or locks for compound actions.
- **Deadlock:** threads wait on each other's locks. Avoid by always acquiring locks in the same order, using timeouts (`tryLock`), and holding locks briefly.
- Also: livelock, starvation, thread-safety of collections.

## Principles (in order of preference)
1. **Don't share** mutable state (confinement, message passing).
2. **Share immutable** state (records, `List.copyOf`).
3. **Use high-level tools**: `ExecutorService`, `CompletableFuture`, `ConcurrentHashMap`, `BlockingQueue`, structured concurrency.
4. Only then use low-level locks, and always `unlock()` in `finally`.

## Java 21+
**Virtual threads** (`Executors.newVirtualThreadPerTaskExecutor()`) make blocking I/O code scale to millions of tasks with simple thread-per-request style. They do not make CPU-bound work faster. Avoid long `synchronized` blocks around blocking calls on older JDKs (pinning; improved in JDK 24).

## Distributed concurrency
Optimistic locking (version column / `ETag`), idempotent consumers, distributed locks only when unavoidable.

## Interview questions
1. `synchronized` vs `ReentrantLock`?
2. What does `volatile` guarantee, and what not?
3. How would you detect and prevent a deadlock?
4. Platform threads vs virtual threads?
