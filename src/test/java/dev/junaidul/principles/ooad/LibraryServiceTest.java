package dev.junaidul.principles.ooad;

import static org.junit.jupiter.api.Assertions.*;

import dev.junaidul.principles.ooad.library.*;
import java.time.*;
import org.junit.jupiter.api.Test;

class LibraryServiceTest {
    private static final Book BOOK = new Book(new Isbn("978-0-13-468599-1"), "Effective Java", "Bloch");

    private static Clock at(String date) {
        return Clock.fixed(LocalDate.parse(date).atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC);
    }

    @Test void borrowThenReturnOnTimeHasNoFine() {
        var repo = new InMemoryLoanRepository();
        var copy = new BookCopy("C1", BOOK);
        var loan = new LibraryService(repo, at("2026-01-01")).borrow(new Member("M1", "Ana", 2), copy);
        assertEquals(LocalDate.parse("2026-01-15"), loan.dueOn());
        assertEquals(BookCopy.Status.ON_LOAN, copy.status());
        assertEquals(0, new LibraryService(repo, at("2026-01-10")).giveBack(copy));
        assertEquals(BookCopy.Status.AVAILABLE, copy.status());
    }

    @Test void lateReturnIsFined() {
        var repo = new InMemoryLoanRepository();
        var copy = new BookCopy("C1", BOOK);
        new LibraryService(repo, at("2026-01-01")).borrow(new Member("M1", "Ana", 2), copy);
        assertEquals(30, new LibraryService(repo, at("2026-01-18")).giveBack(copy));
    }

    @Test void rulesAreEnforced() {
        var service = new LibraryService(new InMemoryLoanRepository(), at("2026-01-01"));
        var member = new Member("M1", "Ana", 1);
        var c1 = new BookCopy("C1", BOOK);
        service.borrow(member, c1);
        assertThrows(IllegalStateException.class, () -> service.borrow(new Member("M2", "Bo", 3), c1));
        assertThrows(IllegalStateException.class, () -> service.borrow(member, new BookCopy("C2", BOOK)));
        assertEquals(new Isbn("9780134685991"), BOOK.isbn()); // value-object equality
    }
}
