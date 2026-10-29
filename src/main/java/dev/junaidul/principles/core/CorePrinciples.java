package dev.junaidul.principles.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Small, focused examples of DRY, KISS, YAGNI, Law of Demeter, Tell Don't Ask,
 * Composition over Inheritance, Command-Query Separation, Fail Fast and
 * Principle of Least Astonishment. Separation of Concerns and the Boy Scout
 * rule are shown across the whole repo (see topics/03-core-principles).
 */
public final class CorePrinciples {
    private CorePrinciples() {}

    // ---------- DRY: one authoritative place for a piece of knowledge ----------
    /** The VAT rule lives here only; every caller reuses it instead of copying 0.15. */
    public static final class Tax {
        public static final double VAT_RATE = 0.15;
        private Tax() {}
        public static double withVat(double net) { return net * (1 + VAT_RATE); }
    }

    // ---------- KISS: the simplest thing that is correct ----------
    /** Readable modulo beats a clever bitwise trick no reviewer can follow. */
    public static boolean isEven(int n) { return n % 2 == 0; }

    // ---------- YAGNI ----------
    // We do NOT add a pluggable "EvenCheckerStrategyFactory" until a second
    // implementation is actually needed. Code you don't write has no bugs.

    // ---------- Law of Demeter + Tell, Don't Ask ----------
    public static final class Wallet {
        private double money;
        public Wallet(double money) { this.money = money; }
        /** Tell the wallet to pay; it decides if it can. */
        public boolean pay(double amount) {
            if (amount > money) return false;
            money -= amount;
            return true;
        }
        public double money() { return money; }
    }
    public static final class Customer {
        private final Wallet wallet;
        public Customer(Wallet wallet) { this.wallet = wallet; }
        /** BAD would be: paperboy calls customer.getWallet().getMoney() and subtracts. */
        public boolean pay(double amount) { return wallet.pay(amount); } // talk to friends only
    }

    // ---------- Composition over Inheritance ----------
    /** Behaviour injected as a part, not inherited from a fragile base class. */
    public interface Engine { String start(); }
    public record ElectricEngine() implements Engine { public String start() { return "silent hum"; } }
    public record PetrolEngine() implements Engine { public String start() { return "vroom"; } }
    public record Car(String model, Engine engine) {
        public String start() { return model + ": " + engine.start(); } // delegate to the part
    }

    // ---------- Command-Query Separation ----------
    public static final class Counter {
        private int value;
        public void increment() { value++; }        // command: changes state, returns nothing
        public int value() { return value; }        // query: returns data, no side effects
    }

    // ---------- Fail Fast ----------
    public record Email(String value) {
        public Email {
            Objects.requireNonNull(value, "email");                       // reject null at once
            if (!value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
                throw new IllegalArgumentException("invalid email: " + value); // not deep inside later
        }
    }

    // ---------- Principle of Least Astonishment ----------
    /** A method named sorted() returns a NEW sorted list; it does not mutate its input. */
    public static List<Integer> sorted(List<Integer> input) {
        var copy = new ArrayList<>(input);
        copy.sort(null);
        return List.copyOf(copy);
    }

    public static void main(String[] args) {
        System.out.println(Tax.withVat(100));
        System.out.println(new Customer(new Wallet(50)).pay(20));
        System.out.println(new Car("Leaf", new ElectricEngine()).start());
        System.out.println(sorted(List.of(3, 1, 2)));
    }
}
