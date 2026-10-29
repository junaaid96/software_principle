package dev.junaidul.principles.ddd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain-Driven Design building blocks for an "Ordering" bounded context.
 * Ubiquitous language: Order, OrderLine, Money, place, cancel. Names match how
 * the business talks.
 */
public final class Ordering {
    private Ordering() {}

    /** Value object: no identity, immutable, equality by value, validates itself. */
    public record Money(BigDecimal amount, String currency) {
        public Money {
            Objects.requireNonNull(currency);
            if (amount.signum() < 0) throw new IllegalArgumentException("negative money");
        }
        public static Money of(String amount, String currency) { return new Money(new BigDecimal(amount), currency); }
        public Money plus(Money other) {
            if (!currency.equals(other.currency)) throw new IllegalArgumentException("currency mismatch");
            return new Money(amount.add(other.amount), currency);
        }
        public Money times(int n) { return new Money(amount.multiply(BigDecimal.valueOf(n)), currency); }
    }

    /** Strongly typed id: you cannot pass a CustomerId where an OrderId is expected. */
    public record OrderId(UUID value) { public static OrderId next() { return new OrderId(UUID.randomUUID()); } }

    /** Entity inside the aggregate. Only reachable through Order. */
    public record OrderLine(String sku, int quantity, Money unitPrice) {
        public OrderLine {
            if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        }
        Money total() { return unitPrice.times(quantity); }
    }

    public enum Status { DRAFT, PLACED, CANCELLED }

    /**
     * Aggregate root: the consistency boundary. All changes go through its methods,
     * so invariants (max 10 lines, single currency, status rules) always hold.
     */
    public static final class Order {
        public static final int MAX_LINES = 10;
        private final OrderId id;
        private final String currency;
        private final List<OrderLine> lines = new ArrayList<>();
        private Status status = Status.DRAFT;

        public Order(OrderId id, String currency) { this.id = id; this.currency = currency; }

        public void addLine(String sku, int qty, Money price) {
            requireStatus(Status.DRAFT);
            if (!price.currency().equals(currency)) throw new IllegalArgumentException("currency mismatch");
            if (lines.size() == MAX_LINES) throw new IllegalStateException("too many lines");
            lines.add(new OrderLine(sku, qty, price));
        }
        public void place() {
            requireStatus(Status.DRAFT);
            if (lines.isEmpty()) throw new IllegalStateException("cannot place empty order");
            status = Status.PLACED;
        }
        public void cancel() {
            if (status == Status.CANCELLED) return;   // idempotent
            status = Status.CANCELLED;
        }
        public Money total() {
            return lines.stream().map(OrderLine::total).reduce(new Money(BigDecimal.ZERO, currency), Money::plus);
        }
        private void requireStatus(Status expected) {
            if (status != expected) throw new IllegalStateException("order is " + status);
        }
        public OrderId id() { return id; }
        public Status status() { return status; }
        public List<OrderLine> lines() { return Collections.unmodifiableList(lines); }
    }

    /** Repository: collection-like access to whole aggregates (never to OrderLine directly). */
    public interface OrderRepository {
        void save(Order order);
        java.util.Optional<Order> byId(OrderId id);
    }
}
