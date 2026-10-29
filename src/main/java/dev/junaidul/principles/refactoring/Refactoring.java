package dev.junaidul.principles.refactoring;

import java.util.List;

/**
 * Refactoring: change structure without changing behaviour. The tests prove
 * before() and after() return identical results (characterization tests).
 * Smells fixed: Long Method, Switch on type code, Primitive Obsession, Duplicate code.
 * Moves applied: Extract Method, Replace Conditional with Polymorphism,
 * Introduce Parameter Object, Replace Magic Number with Constant.
 */
public final class Refactoring {
    private Refactoring() {}

    // ---------- BEFORE ----------
    /** type: 0=regular, 1=new release, 2=children. Rental price in cents. */
    public static int before(int type, int days) {
        int amount = 0;
        if (type == 0) {
            amount += 200;
            if (days > 2) amount += (days - 2) * 150;
        } else if (type == 1) {
            amount += days * 300;
        } else if (type == 2) {
            amount += 150;
            if (days > 3) amount += (days - 3) * 150;
        }
        return amount;
    }

    // ---------- AFTER ----------
    /** Replace type code with polymorphism: each category owns its pricing rule. */
    public enum Category {
        REGULAR { int price(int days) { return tiered(200, 2, days); } },
        NEW_RELEASE { int price(int days) { return days * 300; } },
        CHILDREN { int price(int days) { return tiered(150, 3, days); } };

        abstract int price(int days);

        private static final int EXTRA_DAY_CENTS = 150;  // magic number named once (DRY)
        /** Extracted method removes the duplicated "base + extra days" logic. */
        private static int tiered(int base, int includedDays, int days) {
            return base + Math.max(0, days - includedDays) * EXTRA_DAY_CENTS;
        }
    }

    /** Parameter object instead of loose (int, int) pairs. */
    public record Rental(Category category, int days) {
        public int price() { return category.price(days); }
    }

    public static int after(List<Rental> rentals) {
        return rentals.stream().mapToInt(Rental::price).sum();
    }
}
