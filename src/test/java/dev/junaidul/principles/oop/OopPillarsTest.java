package dev.junaidul.principles.oop;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class OopPillarsTest {
    @Test void encapsulationProtectsInvariant() {
        var acc = new OopPillars.BankAccount();
        acc.deposit(BigDecimal.TEN);
        assertThrows(IllegalStateException.class, () -> acc.withdraw(new BigDecimal("11")));
        assertThrows(UnsupportedOperationException.class, () -> acc.history().clear());
        assertEquals(BigDecimal.TEN, acc.balance());
    }
    @Test void polymorphismDispatchesPerType() {
        var out = OopPillars.checkout(List.of(new OopPillars.VisaCard("1111"),
                new OopPillars.MobileWallet("Nagad")), BigDecimal.ONE);
        assertEquals("VISA ****1111 charged 1", out.get(0));
        assertEquals("Nagad wallet paid 1", out.get(1));
    }
}
