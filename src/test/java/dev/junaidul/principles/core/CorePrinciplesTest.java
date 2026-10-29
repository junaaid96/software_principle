package dev.junaidul.principles.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CorePrinciplesTest {
    @Test void dryVat() { assertEquals(115.0, CorePrinciples.Tax.withVat(100), 1e-9); }
    @Test void kiss() { assertTrue(CorePrinciples.isEven(4)); assertFalse(CorePrinciples.isEven(-3)); }
    @Test void tellDontAsk() {
        var wallet = new CorePrinciples.Wallet(10);
        var c = new CorePrinciples.Customer(wallet);
        assertTrue(c.pay(4));
        assertFalse(c.pay(100));
        assertEquals(6, wallet.money(), 1e-9);
    }
    @Test void composition() {
        assertEquals("Civic: vroom", new CorePrinciples.Car("Civic", new CorePrinciples.PetrolEngine()).start());
    }
    @Test void cqs() {
        var c = new CorePrinciples.Counter();
        c.increment(); c.increment();
        assertEquals(2, c.value());
        assertEquals(2, c.value()); // querying twice changes nothing
    }
    @Test void failFast() {
        assertThrows(IllegalArgumentException.class, () -> new CorePrinciples.Email("nope"));
        assertDoesNotThrow(() -> new CorePrinciples.Email("a@b.io"));
    }
    @Test void leastAstonishmentDoesNotMutateInput() {
        var input = new ArrayList<>(List.of(3, 1, 2));
        assertEquals(List.of(1, 2, 3), CorePrinciples.sorted(input));
        assertEquals(List.of(3, 1, 2), input);
    }
}
