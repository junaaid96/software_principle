package dev.junaidul.principles.ooad.library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Association class between Member and BookCopy, carrying the dates of the loan. */
public record Loan(Member member, BookCopy copy, LocalDate borrowedOn, LocalDate dueOn) {
    /** Fine rule from the requirements: a fixed amount per late day, never negative. */
    public long fineOn(LocalDate returnedOn, long finePerDay) {
        long lateDays = ChronoUnit.DAYS.between(dueOn, returnedOn);
        return Math.max(0, lateDays) * finePerDay;
    }
}
