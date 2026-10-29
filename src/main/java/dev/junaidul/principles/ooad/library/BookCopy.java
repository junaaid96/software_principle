package dev.junaidul.principles.ooad.library;

/**
 * A physical copy of a Book. Entity: has identity (copyId) and a lifecycle
 * modelled by a small state machine (see docs/diagrams/library.md).
 */
public final class BookCopy {
    public enum Status { AVAILABLE, ON_LOAN, LOST }

    private final String copyId;
    private final Book book;
    private Status status = Status.AVAILABLE;

    public BookCopy(String copyId, Book book) { this.copyId = copyId; this.book = book; }

    /** Transition AVAILABLE -> ON_LOAN; any other start state is a rule violation. */
    void checkOut() {
        if (status != Status.AVAILABLE) throw new IllegalStateException(copyId + " is " + status);
        status = Status.ON_LOAN;
    }
    /** Transition ON_LOAN -> AVAILABLE. */
    void checkIn() {
        if (status != Status.ON_LOAN) throw new IllegalStateException(copyId + " is not on loan");
        status = Status.AVAILABLE;
    }
    void markLost() { status = Status.LOST; }

    public String copyId() { return copyId; }
    public Book book() { return book; }
    public Status status() { return status; }
}
