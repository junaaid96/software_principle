package dev.junaidul.principles.ddd;

import static org.junit.jupiter.api.Assertions.*;

import dev.junaidul.principles.ddd.Ordering.*;
import org.junit.jupiter.api.Test;

class OrderingTest {
    @Test void aggregateEnforcesInvariants() {
        var order = new Order(OrderId.next(), "USD");
        assertThrows(IllegalStateException.class, order::place);              // empty
        order.addLine("PEN", 3, Money.of("10", "USD"));
        assertThrows(IllegalArgumentException.class, () -> order.addLine("X", 1, Money.of("1", "EUR")));
        order.place();
        assertEquals(Money.of("30", "USD"), order.total());                   // value equality
        assertThrows(IllegalStateException.class, () -> order.addLine("Y", 1, Money.of("1", "USD")));
        order.cancel(); order.cancel();                                       // idempotent
        assertEquals(Status.CANCELLED, order.status());
        assertThrows(UnsupportedOperationException.class, () -> order.lines().clear());
    }
}
