package dev.junaidul.principles.oop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** The four OOP pillars: encapsulation, abstraction, inheritance, polymorphism. */
public final class OopPillars {
    private OopPillars() {}

    // ---------- Encapsulation: hide state, expose behaviour that keeps invariants ----------
    public static final class BankAccount {
        private BigDecimal balance = BigDecimal.ZERO;            // private: no direct writes
        private final List<String> history = new ArrayList<>();

        public void deposit(BigDecimal amount) {
            requirePositive(amount);                             // invariant guarded in one place
            balance = balance.add(amount);
            history.add("deposit " + amount);
        }
        public void withdraw(BigDecimal amount) {
            requirePositive(amount);
            if (balance.compareTo(amount) < 0) throw new IllegalStateException("insufficient funds");
            balance = balance.subtract(amount);
            history.add("withdraw " + amount);
        }
        public BigDecimal balance() { return balance; }
        /** Return a read-only view so callers cannot mutate our internal list. */
        public List<String> history() { return Collections.unmodifiableList(history); }

        private static void requirePositive(BigDecimal amount) {
            if (amount.signum() <= 0) throw new IllegalArgumentException("amount must be > 0");
        }
    }

    // ---------- Abstraction: callers see WHAT, not HOW ----------
    public interface PaymentMethod { String pay(BigDecimal amount); }

    // ---------- Inheritance: reuse a template, override a step ----------
    public abstract static class Card implements PaymentMethod {
        private final String last4;
        protected Card(String last4) { this.last4 = last4; }
        /** Template: shared flow, subclasses provide only the network name. */
        @Override public final String pay(BigDecimal amount) {
            return network() + " ****" + last4 + " charged " + amount;
        }
        protected abstract String network();
    }
    public static final class VisaCard extends Card {
        public VisaCard(String last4) { super(last4); }
        @Override protected String network() { return "VISA"; }
    }

    /** Composition-friendly alternative: a record implementing the interface directly. */
    public record MobileWallet(String provider) implements PaymentMethod {
        public String pay(BigDecimal amount) { return provider + " wallet paid " + amount; }
    }

    // ---------- Polymorphism: one call site, many behaviours ----------
    public static List<String> checkout(List<PaymentMethod> methods, BigDecimal amount) {
        return methods.stream().map(m -> m.pay(amount)).toList(); // dynamic dispatch
    }

    public static void main(String[] args) {
        var acc = new BankAccount();
        acc.deposit(new BigDecimal("100"));
        acc.withdraw(new BigDecimal("30"));
        System.out.println(acc.balance() + " " + acc.history());
        checkout(List.of(new VisaCard("4242"), new MobileWallet("PayPal")), new BigDecimal("9.99"))
                .forEach(System.out::println);
    }
}
