package dev.junaidul.principles.ooad.library;

/** Catalogue entry (the "title"), as found in the domain model. */
public record Book(Isbn isbn, String title, String author) {}
