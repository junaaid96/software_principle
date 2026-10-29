package dev.junaidul.principles.patterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/** Structural patterns: how classes and objects are COMPOSED. */
public final class Structural {
    private Structural() {}

    // ---------- Adapter: make an incompatible API fit the interface we expect ----------
    public interface PaymentGateway { boolean charge(String customer, long cents); }
    /** Third-party SDK we cannot change: different names and units (dollars as double). */
    public static final class LegacyPaySdk {
        public String makePayment(double dollars, String user) { return dollars > 0 ? "OK" : "FAIL"; }
    }
    public static final class LegacyPayAdapter implements PaymentGateway {
        private final LegacyPaySdk sdk;
        public LegacyPayAdapter(LegacyPaySdk sdk) { this.sdk = sdk; }
        public boolean charge(String customer, long cents) {
            return "OK".equals(sdk.makePayment(cents / 100.0, customer)); // translate units + result
        }
    }

    // ---------- Decorator: add behaviour by wrapping, without subclass explosion ----------
    public interface DataSource { String read(); }
    public record StringSource(String data) implements DataSource { public String read() { return data; } }
    public record UpperCase(DataSource inner) implements DataSource {
        public String read() { return inner.read().toUpperCase(); }
    }
    public record Trimmed(DataSource inner) implements DataSource {
        public String read() { return inner.read().trim(); }
    }

    // ---------- Facade: one simple entry point over a complex subsystem ----------
    public static final class Inventory { boolean reserve(String sku) { return !sku.isBlank(); } }
    public static final class Billing { boolean bill(String user) { return !user.isBlank(); } }
    public static final class Shipping { String ship(String sku) { return "TRACK-" + sku; } }
    public static final class OrderFacade {
        private final Inventory inventory = new Inventory();
        private final Billing billing = new Billing();
        private final Shipping shipping = new Shipping();
        /** Callers do one call; the facade orchestrates three subsystems in the right order. */
        public String placeOrder(String user, String sku) {
            if (!inventory.reserve(sku)) return "OUT_OF_STOCK";
            if (!billing.bill(user)) return "PAYMENT_FAILED";
            return shipping.ship(sku);
        }
    }

    // ---------- Proxy: same interface, controls access (here: caching) ----------
    public static final class CachingProxy<K, V> implements Function<K, V> {
        private final Function<K, V> real;
        private final Map<K, V> cache = new HashMap<>();
        public int realCalls;                                  // exposed for the demo/test
        public CachingProxy(Function<K, V> real) { this.real = real; }
        public V apply(K key) {
            return cache.computeIfAbsent(key, k -> { realCalls++; return real.apply(k); });
        }
    }

    // ---------- Composite: treat a tree of objects and a single object the same ----------
    public sealed interface FileNode permits FileLeaf, Folder { long size(); }
    public record FileLeaf(String name, long size) implements FileNode {}
    public record Folder(String name, List<FileNode> children) implements FileNode {
        public Folder(String name) { this(name, new ArrayList<>()); }
        public Folder add(FileNode n) { children.add(n); return this; }
        public long size() { return children.stream().mapToLong(FileNode::size).sum(); } // recursion
    }
}
