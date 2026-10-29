package dev.junaidul.principles.concurrency;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/** Concurrency principles: avoid shared mutable state, or guard it explicitly. */
public final class Concurrency {
    private Concurrency() {}

    /** BROKEN: count++ is read-modify-write, so concurrent threads lose updates. */
    public static final class UnsafeCounter { int count; void inc() { count++; } }

    /** Fix 1: atomic variables for single values. */
    public static final class AtomicCounter {
        private final AtomicInteger count = new AtomicInteger();
        public void inc() { count.incrementAndGet(); }
        public int get() { return count.get(); }
    }

    /** Fix 2: an explicit lock for compound invariants (always unlock in finally). */
    public static final class Account {
        private final ReentrantLock lock = new ReentrantLock();
        private long balance;
        public void deposit(long amount) {
            lock.lock();
            try { balance += amount; } finally { lock.unlock(); }
        }
        public long balance() {
            lock.lock();
            try { return balance; } finally { lock.unlock(); }
        }
    }

    /** Run n increments on virtual threads (Java 21) and wait for all of them. */
    public static int countWithVirtualThreads(int n) {
        var counter = new AtomicCounter();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) { // closes = waits for tasks
            IntStream.range(0, n).forEach(i -> executor.submit(counter::inc));
        }
        return counter.get();
    }

    /** Compose async work without blocking threads; join only at the edge. */
    public static int parallelSum(List<Integer> parts) {
        var futures = parts.stream().map(p -> CompletableFuture.supplyAsync(() -> p * 2)).toList();
        return futures.stream().mapToInt(CompletableFuture::join).sum();
    }

    /** Concurrent collections: merge is atomic per key. */
    public static ConcurrentHashMap<String, Integer> wordCount(List<String> words) {
        var map = new ConcurrentHashMap<String, Integer>();
        words.parallelStream().forEach(w -> map.merge(w, 1, Integer::sum));
        return map;
    }
}
