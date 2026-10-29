package dev.junaidul.principles.cleancode;

import java.util.List;
import java.util.Optional;

/** Clean code: intention-revealing names, small functions, guard clauses, honest errors. */
public final class CleanCode {
    private CleanCode() {}

    // ---------- BAD ----------
    // Cryptic names, magic numbers, nested ifs, null as an error signal, a lying comment.
    static Double calc(List<Double> l, int t) {
        if (l != null) {
            if (!l.isEmpty()) {
                double s = 0;
                for (Double d : l) s += d;
                if (t == 1) return s * 0.9; // 5% discount  <- comment no longer true
                return s;
            }
        }
        return null;
    }

    // ---------- GOOD ----------
    public enum CustomerTier { REGULAR, GOLD }
    private static final double GOLD_DISCOUNT_RATE = 0.10;          // named constant, not 0.9 magic

    /** Name says what it returns. Optional makes "no total" explicit instead of null. */
    public static Optional<Double> orderTotal(List<Double> itemPrices, CustomerTier tier) {
        if (itemPrices == null || itemPrices.isEmpty()) return Optional.empty(); // guard clause, flat code
        double subtotal = sum(itemPrices);
        return Optional.of(applyTierDiscount(subtotal, tier));
    }

    /** Small function, one level of abstraction. */
    private static double sum(List<Double> prices) {
        return prices.stream().mapToDouble(Double::doubleValue).sum();
    }

    private static double applyTierDiscount(double subtotal, CustomerTier tier) {
        return tier == CustomerTier.GOLD ? subtotal * (1 - GOLD_DISCOUNT_RATE) : subtotal;
    }

    /** Error handling: specific exception with context; never swallow and continue. */
    public static int parsePort(String raw) {
        try {
            int port = Integer.parseInt(raw.trim());
            if (port < 1 || port > 65_535) throw new IllegalArgumentException("port out of range: " + port);
            return port;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("port is not a number: '" + raw + "'", e); // keep the cause
        }
    }
}
