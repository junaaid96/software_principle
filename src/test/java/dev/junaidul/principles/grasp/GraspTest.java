package dev.junaidul.principles.grasp;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class GraspTest {
    @Test void controllerDrivesSaleAndExpertComputesTotal() {
        var reg = new Grasp.Register(new Grasp.FlatTax(new BigDecimal("0.10")));
        reg.startSale();
        reg.enterItem(new Grasp.Product("A", new BigDecimal("10")), 2);
        reg.enterItem(new Grasp.Product("B", new BigDecimal("5")), 1);
        assertEquals(0, new BigDecimal("27.5").compareTo(reg.endSale()));
        assertEquals("2", reg.receipt(s -> String.valueOf(s.lines().size())));
    }
}
