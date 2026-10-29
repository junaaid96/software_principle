package dev.junaidul.principles.testing;

import java.time.Clock;
import java.time.LocalTime;

/**
 * Code designed for testability: dependencies (clock, sender) are injected, so
 * tests can use fakes/stubs/mocks. Built test-first; see TestingTest for the
 * red-green-refactor steps, a hand-written mock, BDD-style naming and a
 * property-based style check.
 */
public final class Testing {
    private Testing() {}

    /** FizzBuzz, the classic TDD kata. Each branch was added by a failing test first. */
    public static String fizzBuzz(int n) {
        if (n % 15 == 0) return "FizzBuzz";
        if (n % 3 == 0) return "Fizz";
        if (n % 5 == 0) return "Buzz";
        return Integer.toString(n);
    }

    public interface MessageSender { void send(String to, String body); }

    /** Greets differently by time of day and sends via an injected port. */
    public static final class Greeter {
        private final Clock clock;
        private final MessageSender sender;
        public Greeter(Clock clock, MessageSender sender) { this.clock = clock; this.sender = sender; }
        public void greet(String user) {
            var hour = LocalTime.now(clock).getHour();
            var greeting = hour < 12 ? "Good morning" : "Good evening";
            sender.send(user, greeting + ", " + user);
        }
    }

    /** Pure function: perfect for property-based tests. */
    public static String reverse(String s) { return new StringBuilder(s).reverse().toString(); }
}
