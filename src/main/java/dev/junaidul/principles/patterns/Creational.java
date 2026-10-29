package dev.junaidul.principles.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/** Creational patterns: control HOW objects are created. */
public final class Creational {
    private Creational() {}

    // ---------- Builder: many optional parameters, readable, immutable result ----------
    public record HttpRequest(String method, String url, Map<String, String> headers, String body) {
        public static Builder builder(String url) { return new Builder(url); }

        public static final class Builder {
            private final String url;              // required
            private String method = "GET";         // sensible defaults for optional parts
            private final java.util.LinkedHashMap<String, String> headers = new java.util.LinkedHashMap<>();
            private String body = "";
            private Builder(String url) { this.url = url; }
            public Builder method(String m) { this.method = m; return this; } // fluent: return this
            public Builder header(String k, String v) { headers.put(k, v); return this; }
            public Builder body(String b) { this.body = b; return this; }
            public HttpRequest build() {
                if (!url.startsWith("http")) throw new IllegalStateException("bad url"); // validate once
                return new HttpRequest(method, url, Map.copyOf(headers), body);
            }
        }
    }

    // ---------- Factory Method (static factory flavour): choose a subtype by input ----------
    public sealed interface Notification permits EmailNotification, SmsNotification, PushNotification {
        String send(String msg);
    }
    public record EmailNotification() implements Notification { public String send(String m) { return "email:" + m; } }
    public record SmsNotification() implements Notification { public String send(String m) { return "sms:" + m; } }
    public record PushNotification() implements Notification { public String send(String m) { return "push:" + m; } }

    public static Notification notificationFor(String channel) {
        // Callers depend on Notification, never on the concrete classes.
        return switch (channel) {
            case "email" -> new EmailNotification();
            case "sms" -> new SmsNotification();
            case "push" -> new PushNotification();
            default -> throw new IllegalArgumentException("unknown channel " + channel);
        };
    }

    // ---------- Abstract Factory: create FAMILIES of related objects ----------
    public interface Button { String render(); }
    public interface Checkbox { String render(); }
    public interface UiFactory { Button button(); Checkbox checkbox(); }
    public static final class DarkUi implements UiFactory {
        public Button button() { return () -> "[dark button]"; }
        public Checkbox checkbox() { return () -> "[dark checkbox]"; }
    }
    public static final class LightUi implements UiFactory {
        public Button button() { return () -> "[light button]"; }
        public Checkbox checkbox() { return () -> "[light checkbox]"; }
    }
    /** Client code never mixes a dark button with a light checkbox. */
    public static List<String> renderForm(UiFactory ui) {
        return List.of(ui.button().render(), ui.checkbox().render());
    }

    // ---------- Singleton: prefer an enum (thread-safe, serialization-safe) ----------
    // Use sparingly: singletons are global state. Prefer DI-managed single instances.
    public enum IdGenerator {
        INSTANCE;
        private final AtomicLong next = new AtomicLong(1);
        public long nextId() { return next.getAndIncrement(); }
    }

    // ---------- Prototype: copy a configured object instead of rebuilding it ----------
    public record Document(String template, List<String> sections) {
        public Document { sections = List.copyOf(sections); }
        /** Copy with a change; the original stays untouched. */
        public Document withSection(String s) {
            var copy = new ArrayList<>(sections);
            copy.add(s);
            return new Document(template, copy);
        }
    }
}
