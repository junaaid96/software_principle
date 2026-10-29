package dev.junaidul.principles.ooad.library;

import java.time.Clock;
import java.time.LocalDate;

/**
 * Application service realising the use cases "Borrow book" and "Return book".
 * The Clock is injected so tests can control "today" (no hidden dependency on time).
 */
public final class LibraryService {
    public static final int LOAN_DAYS = 14;
    public static final long FINE_PER_DAY = 10;

    private final LoanRepository loans;
    private final Clock clock;

    public LibraryService(LoanRepository loans, Clock clock) {
        this.loans = loans;
        this.clock = clock;
    }

    /** Use case UC-1 Borrow book. Each guard is one business rule from the requirements. */
    public Loan borrow(Member member, BookCopy copy) {
        if (loans.findActiveByMember(member.memberId()).size() >= member.maxLoans())
            throw new IllegalStateException("loan limit reached"); // rule R2
        copy.checkOut();                                            // rule R1: copy must be available
        var today = LocalDate.now(clock);
        var loan = new Loan(member, copy, today, today.plusDays(LOAN_DAYS));
        loans.save(loan);
        return loan;
    }

    /** Use case UC-2 Return book. Returns the fine owed (0 if on time). */
    public long giveBack(BookCopy copy) {
        var loan = loans.findActiveByCopy(copy.copyId())
                .orElseThrow(() -> new IllegalStateException("no active loan"));
        copy.checkIn();
        loans.remove(loan);
        return loan.fineOn(LocalDate.now(clock), FINE_PER_DAY);    // rule R3: late fine
    }
}
