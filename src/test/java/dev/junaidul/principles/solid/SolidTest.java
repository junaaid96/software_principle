package dev.junaidul.principles.solid;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class SolidTest {
    @Test void srpInvoiceRejectsNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> new Srp.Invoice("x", -1));
        assertEquals("Invoice A: 2.50", new Srp.InvoiceFormatter().format(new Srp.Invoice("A", 2.5)));
    }
    @Test void ocpNewShapeWorksWithoutChangingCalculator() {
        assertEquals(10.0, Ocp.totalArea(List.of(new Ocp.Rectangle(2, 3), new Ocp.Square(2))), 1e-9);
    }
    @Test void lspViolationIsObservable() {
        assertEquals(20, Lsp.stretch(new Lsp.MutableRectangle()));
        assertNotEquals(20, Lsp.stretch(new Lsp.MutableSquare())); // the bug LSP warns about
    }
    @Test void ispClientNeedsOnlyPrinter() {
        assertEquals("printed report", Isp.printReport(new Isp.OfficeMachine()));
    }
    @Test void dipServiceIsTestableWithFakeStore() {
        var service = new Dip.SignupService(new Dip.InMemoryUserStore());
        assertTrue(service.signup("a@b.c"));
        assertFalse(service.signup("a@b.c"));
    }
}
