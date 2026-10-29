package dev.junaidul.principles.testing;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TestingTest {
    // ----- TDD: these tests were written one at a time, each failing first -----
    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource({"1,1", "3,Fizz", "5,Buzz", "15,FizzBuzz", "98,98"})
    void fizzBuzz(int n, String expected) { assertEquals(expected, Testing.fizzBuzz(n)); }

    // ----- Test doubles: a stub (fixed Clock) and a hand-written mock that records calls -----
    static final class RecordingSender implements Testing.MessageSender {
        final List<String> sent = new ArrayList<>();
        public void send(String to, String body) { sent.add(to + "|" + body); }
    }

    @Nested @DisplayName("Given a greeter")
    class GreeterBehaviour {                                       // BDD: Given / When / Then
        @Test @DisplayName("when it is 09:00, then it says good morning")
        void morning() {
            var clock = Clock.fixed(Instant.parse("2026-01-01T09:00:00Z"), ZoneOffset.UTC); // stub
            var mock = new RecordingSender();
            new Testing.Greeter(clock, mock).greet("Ana");                                   // when
            assertEquals(List.of("Ana|Good morning, Ana"), mock.sent);                       // then: verify interaction
        }
        @Test @DisplayName("when it is 20:00, then it says good evening")
        void evening() {
            var mock = new RecordingSender();
            new Testing.Greeter(Clock.fixed(Instant.parse("2026-01-01T20:00:00Z"), ZoneOffset.UTC), mock).greet("Bo");
            assertTrue(mock.sent.get(0).contains("Good evening"));
        }
    }

    // ----- Property-based style: check an invariant over many random inputs -----
    @Test void reversingTwiceGivesTheOriginal() {
        var random = new Random(42);                               // fixed seed = reproducible failures
        for (int i = 0; i < 1_000; i++) {
            var s = random.ints(random.nextInt(20), 32, 127)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
            assertEquals(s, Testing.reverse(Testing.reverse(s)));
            assertEquals(s.length(), Testing.reverse(s).length());
        }
    }
}
