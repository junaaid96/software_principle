package dev.junaidul.principles.solid;

import java.util.ArrayList;
import java.util.List;

/**
 * Single Responsibility Principle: a module should have one reason to change
 * (one actor that asks for changes).
 */
public final class Srp {
    private Srp() {}

    // ---------- BAD: one class owns data, formatting, persistence and email ----------
    /** Changes for the finance team, the DBA and the email team all land here. */
    static final class InvoiceGodClass {
        final double amount;                       // business data
        InvoiceGodClass(double amount) { this.amount = amount; }
        String print() { return "Invoice: " + amount; }          // presentation concern
        void save() { /* SQL here */ }                           // persistence concern
        void email() { /* SMTP here */ }                         // notification concern
    }

    // ---------- GOOD: each class has exactly one reason to change ----------
    /** Pure business data plus its own invariant. */
    public record Invoice(String id, double amount) {
        public Invoice {
            // Fail fast: an invoice can never be negative.
            if (amount < 0) throw new IllegalArgumentException("amount must be >= 0");
        }
    }

    /** Only knows how to turn an invoice into text. */
    public static final class InvoiceFormatter {
        public String format(Invoice invoice) {
            return "Invoice %s: %.2f".formatted(invoice.id(), invoice.amount());
        }
    }

    /** Only knows how to store invoices (in memory for the demo). */
    public static final class InvoiceRepository {
        private final List<Invoice> store = new ArrayList<>();
        public void save(Invoice invoice) { store.add(invoice); }
        public int count() { return store.size(); }
    }

    public static void main(String[] args) {
        var invoice = new Invoice("INV-1", 120.5);       // create the data
        var repo = new InvoiceRepository();
        repo.save(invoice);                              // persistence is delegated
        System.out.println(new InvoiceFormatter().format(invoice)); // formatting is delegated
    }
}
