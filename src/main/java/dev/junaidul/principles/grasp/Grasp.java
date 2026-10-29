package dev.junaidul.principles.grasp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * GRASP (General Responsibility Assignment Software Patterns), shown on a tiny
 * point-of-sale model. Each comment names the pattern that decided WHO does WHAT.
 */
public final class Grasp {
    private Grasp() {}

    /** Information Expert: the product knows its own price. */
    public record Product(String sku, BigDecimal price) {}

    /** Information Expert: a line knows product + quantity, so it computes its subtotal. */
    public record SaleLine(Product product, int quantity) {
        public BigDecimal subtotal() { return product.price().multiply(BigDecimal.valueOf(quantity)); }
    }

    /** Protected Variations / Polymorphism: tax rules vary, so hide them behind an interface. */
    public interface TaxPolicy { BigDecimal taxOn(BigDecimal amount); }
    public record FlatTax(BigDecimal rate) implements TaxPolicy {
        public BigDecimal taxOn(BigDecimal amount) { return amount.multiply(rate); }
    }

    /** Pure Fabrication: not a domain concept, invented to keep Sale free of I/O. */
    public interface ReceiptPrinter { String print(Sale sale); }

    public static final class Sale {
        private final List<SaleLine> lines = new ArrayList<>();
        private final TaxPolicy tax;
        Sale(TaxPolicy tax) { this.tax = tax; }

        /** Creator: Sale aggregates lines, so Sale creates them. */
        public void addLine(Product p, int qty) { lines.add(new SaleLine(p, qty)); }

        /** Information Expert: Sale owns the lines, so it computes the total. High cohesion. */
        public BigDecimal total() {
            var net = lines.stream().map(SaleLine::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
            return net.add(tax.taxOn(net));
        }
        public List<SaleLine> lines() { return List.copyOf(lines); }
    }

    /**
     * Controller: the first object behind the UI that receives a system event.
     * Low Coupling + Indirection: the UI talks only to this, never to Sale internals.
     */
    public static final class Register {
        private final TaxPolicy tax;
        private Sale current;
        public Register(TaxPolicy tax) { this.tax = tax; }
        public void startSale() { current = new Sale(tax); }          // Creator again
        public void enterItem(Product p, int qty) { current.addLine(p, qty); }
        public BigDecimal endSale() { return current.total(); }
        public String receipt(ReceiptPrinter printer) { return printer.print(current); }
    }

    public static void main(String[] args) {
        var reg = new Register(new FlatTax(new BigDecimal("0.10")));
        reg.startSale();
        reg.enterItem(new Product("TEA", new BigDecimal("2.00")), 3);
        System.out.println(reg.endSale()); // 6.00 + 10% = 6.6000
        System.out.println(reg.receipt(s -> "lines=" + s.lines().size() + " total=" + s.total()));
    }
}
