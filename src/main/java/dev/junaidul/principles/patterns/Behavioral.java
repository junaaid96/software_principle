package dev.junaidul.principles.patterns;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

/** Behavioral patterns: how objects COMMUNICATE and share responsibilities. */
public final class Behavioral {
    private Behavioral() {}

    // ---------- Strategy: swap an algorithm at runtime ----------
    @FunctionalInterface public interface DiscountStrategy { double apply(double price); }
    public static final DiscountStrategy NONE = p -> p;
    public static final DiscountStrategy BLACK_FRIDAY = p -> p * 0.7;
    public static DiscountStrategy fixedOff(double off) { return p -> Math.max(0, p - off); }

    // ---------- Observer: publish once, many subscribers react ----------
    public static final class EventBus<E> {
        private final List<Consumer<E>> subscribers = new ArrayList<>();
        public void subscribe(Consumer<E> s) { subscribers.add(s); }
        public void publish(E event) { subscribers.forEach(s -> s.accept(event)); }
    }

    // ---------- Command: an action as an object (undo, queue, log) ----------
    public interface Command { void execute(); void undo(); }
    public static final class TextEditor {
        private final StringBuilder text = new StringBuilder();
        private final Deque<Command> history = new ArrayDeque<>();
        public void run(Command c) { c.execute(); history.push(c); }
        public void undo() { if (!history.isEmpty()) history.pop().undo(); }
        public Command append(String s) {
            return new Command() {
                public void execute() { text.append(s); }
                public void undo() { text.setLength(text.length() - s.length()); }
            };
        }
        public String text() { return text.toString(); }
    }

    // ---------- Template Method: fixed skeleton, customizable steps ----------
    public abstract static class DataExporter {
        /** final: subclasses cannot break the order of the steps. */
        public final String export(List<String> rows) {
            return header() + String.join(separator(), rows) + footer();
        }
        protected String header() { return ""; }
        protected String footer() { return ""; }
        protected abstract String separator();
    }
    public static final class CsvExporter extends DataExporter {
        protected String header() { return "name\n"; }
        protected String separator() { return "\n"; }
    }

    // ---------- State: behaviour changes with internal state, no giant if/else ----------
    public sealed interface OrderState permits Created, Paid, Shipped {
        OrderState next();
    }
    public record Created() implements OrderState { public OrderState next() { return new Paid(); } }
    public record Paid() implements OrderState { public OrderState next() { return new Shipped(); } }
    public record Shipped() implements OrderState {
        public OrderState next() { throw new IllegalStateException("already shipped"); }
    }

    // ---------- Chain of Responsibility: pass a request along handlers ----------
    public interface Handler { String handle(int amount); }
    public static Handler approvalChain() {
        Handler director = a -> a <= 100_000 ? "director" : "board";
        Handler manager = a -> a <= 10_000 ? "manager" : director.handle(a);
        return a -> a <= 1_000 ? "team-lead" : manager.handle(a);   // first link in the chain
    }
}
