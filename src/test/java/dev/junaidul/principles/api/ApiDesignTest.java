package dev.junaidul.principles.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Test;

class ApiDesignTest {
    @Test void idempotentRetryDoesNotRepeatSideEffect() {
        var store = new ApiDesign.IdempotencyStore<String>();
        var first = store.execute("key-1", () -> "payment-123");
        var retry = store.execute("key-1", () -> "payment-456");
        assertEquals(first, retry);
        assertEquals(1, store.executions);
    }
    @Test void cursorPaginationWalksAllItems() {
        var all = LongStream.rangeClosed(1, 5).mapToObj(i -> new ApiDesign.Item(i, "n" + i)).toList();
        var p1 = ApiDesign.page(all, Optional.empty(), 2);
        assertEquals(Optional.of("2"), p1.nextCursor());
        var p3 = ApiDesign.page(all, Optional.of("4"), 2);
        assertEquals(1, p3.items().size());
        assertTrue(p3.nextCursor().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> ApiDesign.page(all, Optional.empty(), 1000));
    }
    @Test void problemDetails() {
        assertEquals(404, ApiDesign.Problem.notFound("order 7").status());
        assertEquals("/v2/orders", ApiDesign.versioned(2, "/orders"));
    }
}
