package dev.junaidul.principles.concurrency;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class ConcurrencyTest {
    @Test void virtualThreadsWithAtomicCounterLoseNothing() {
        assertEquals(10_000, Concurrency.countWithVirtualThreads(10_000));
    }
    @Test void lockedAccountIsConsistent() throws Exception {
        var account = new Concurrency.Account();
        try (var pool = Executors.newFixedThreadPool(8)) {
            IntStream.range(0, 1_000).forEach(i -> pool.submit(() -> account.deposit(1)));
        }
        assertEquals(1_000, account.balance());
    }
    @Test void asyncAndConcurrentMap() {
        assertEquals(12, Concurrency.parallelSum(List.of(1, 2, 3)));
        assertEquals(2, Concurrency.wordCount(List.of("a", "b", "a")).get("a"));
    }
}
