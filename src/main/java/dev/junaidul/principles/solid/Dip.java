package dev.junaidul.principles.solid;

import java.util.ArrayList;
import java.util.List;

/**
 * Dependency Inversion Principle: high-level policy depends on abstractions,
 * and low-level details implement those abstractions. Combine with
 * constructor injection so dependencies are explicit and swappable in tests.
 */
public final class Dip {
    private Dip() {}

    // ---------- BAD: the policy creates its own concrete detail ----------
    static final class MySqlDatabase { void insert(String row) { /* JDBC */ } }
    static final class BadSignupService {
        private final MySqlDatabase db = new MySqlDatabase(); // hard-wired, untestable
        void signup(String email) { db.insert(email); }
    }

    // ---------- GOOD: the policy owns the interface, details plug in ----------
    /** Abstraction owned by the high-level module. */
    public interface UserStore { void save(String email); boolean exists(String email); }

    /** High-level policy: no idea whether storage is SQL, Mongo or memory. */
    public static final class SignupService {
        private final UserStore store;
        public SignupService(UserStore store) { this.store = store; } // constructor injection
        public boolean signup(String email) {
            if (store.exists(email)) return false; // business rule: no duplicates
            store.save(email);
            return true;
        }
    }

    /** Low-level detail implementing the abstraction (used by tests too). */
    public static final class InMemoryUserStore implements UserStore {
        private final List<String> emails = new ArrayList<>();
        public void save(String email) { emails.add(email); }
        public boolean exists(String email) { return emails.contains(email); }
    }

    public static void main(String[] args) {
        var service = new SignupService(new InMemoryUserStore()); // wiring happens at the edge
        System.out.println(service.signup("a@example.com")); // true
        System.out.println(service.signup("a@example.com")); // false, duplicate
        new BadSignupService().signup("x@example.com");
    }
}
