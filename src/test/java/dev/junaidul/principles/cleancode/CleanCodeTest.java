package dev.junaidul.principles.cleancode;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CleanCodeTest {
    @Test void totals() {
        assertEquals(Optional.empty(), CleanCode.orderTotal(List.of(), CleanCode.CustomerTier.GOLD));
        assertEquals(90.0, CleanCode.orderTotal(List.of(40.0, 60.0), CleanCode.CustomerTier.GOLD).orElseThrow(), 1e-9);
        assertEquals(100.0, CleanCode.orderTotal(List.of(40.0, 60.0), CleanCode.CustomerTier.REGULAR).orElseThrow(), 1e-9);
    }
    @Test void errorsCarryContext() {
        assertEquals(8080, CleanCode.parsePort(" 8080 "));
        var e = assertThrows(IllegalArgumentException.class, () -> CleanCode.parsePort("abc"));
        assertInstanceOf(NumberFormatException.class, e.getCause());
        assertThrows(IllegalArgumentException.class, () -> CleanCode.parsePort("70000"));
    }
}
