package dev.junaidul.principles.ooad.library;

/** Value object: identified by its value, immutable, validated on creation. */
public record Isbn(String value) {
    public Isbn {
        // Keep only digits and X so "978-0-13-468599-1" and "9780134685991" are equal.
        value = value == null ? "" : value.replaceAll("[^0-9X]", "");
        if (value.length() != 10 && value.length() != 13)
            throw new IllegalArgumentException("ISBN must have 10 or 13 digits");
    }
}
