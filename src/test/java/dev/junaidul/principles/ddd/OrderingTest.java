package dev.junaidul.principles.ddd;

import static org.junit.jupiter.api.Assertions.*;

import dev.junaidul.principles.ddd.Ordering.*;
import org.junit.jupiter.api.Test;

class OrderingTest {
    @Test void aggregateEnforcesInvariants() {
        var order = new Order(OrderId.next(), "BDT");
        assertThrows(IllegalStateException.class, order::place);              // empty
        order.addLine("PEN", 3, Money.of("10", "BDT"));
        assertThrows(IllegalArgumentException.class, () -> order.addLine("X", 1, Money.of("1", "USD")));
        order.place();
        assertEquals(Money.of("30", "BDT"), order.total());                   // value equality
        assertThrows(IllegalStateException.class, () -> order.addLine("Y", 1, Money.of("1", "BDT")));
        order.cancel(); order.cancel();                                       // idempotent
        assertEquals(Status.CANCELLED, order.status());
        assertThrows(UnsupportedOperationException.class, () -> order.lines().clear());
    }
}
