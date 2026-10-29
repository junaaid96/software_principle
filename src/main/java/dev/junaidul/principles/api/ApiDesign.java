package dev.junaidul.principles.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Framework-free models of REST API best practices: idempotency keys, cursor
 * pagination and RFC 9457 "problem details" error bodies.
 */
public final class ApiDesign {
    private ApiDesign() {}

    /** RFC 9457 error model: machine-readable type + human title + per-request detail. */
    public record Problem(String type, String title, int status, String detail) {
        public static Problem notFound(String what) {
            return new Problem("https://example.com/problems/not-found", "Resource not found", 404, what + " does not exist");
        }
        public static Problem validation(String detail) {
            return new Problem("https://example.com/problems/validation", "Validation failed", 422, detail);
        }
    }

    /**
     * Idempotency: a client retries POST /payments with the same Idempotency-Key
     * header; the server returns the stored first response instead of charging twice.
     */
    public static final class IdempotencyStore<R> {
        private final Map<String, R> responses = new HashMap<>();
        public int executions;                                   // for the demo/test
        public synchronized R execute(String key, Supplier<R> operation) {
            return responses.computeIfAbsent(key, k -> { executions++; return operation.get(); });
        }
    }

    /** Cursor (keyset) pagination: stable under inserts, O(page) instead of OFFSET scans. */
    public record Page<T>(List<T> items, Optional<String> nextCursor) {}
    public record Item(long id, String name) {}

    public static Page<Item> page(List<Item> all, Optional<String> cursor, int limit) {
        if (limit < 1 || limit > 100) throw new IllegalArgumentException("limit must be 1..100"); // cap page size
        long after = cursor.map(Long::parseLong).orElse(0L);
        var sorted = new ArrayList<>(all);
        sorted.sort(Comparator.comparingLong(Item::id));                 // stable ordering is mandatory
        var items = sorted.stream().filter(i -> i.id() > after).limit(limit + 1L).toList(); // fetch one extra
        boolean hasMore = items.size() > limit;
        var pageItems = hasMore ? items.subList(0, limit) : items;
        var next = hasMore ? Optional.of(Long.toString(pageItems.get(limit - 1).id())) : Optional.<String>empty();
        return new Page<>(pageItems, next);
    }

    /** URI versioning helper: /v1/... vs /v2/...; never break existing clients in place. */
    public static String versioned(int version, String path) { return "/v" + version + path; }
}
