package dev.junaidul.principles.ooad.library;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Simple adapter used by the demo and the tests. */
public final class InMemoryLoanRepository implements LoanRepository {
    private final List<Loan> loans = new ArrayList<>();
    public void save(Loan loan) { loans.add(loan); }
    public void remove(Loan loan) { loans.remove(loan); }
    public Optional<Loan> findActiveByCopy(String copyId) {
        return loans.stream().filter(l -> l.copy().copyId().equals(copyId)).findFirst();
    }
    public List<Loan> findActiveByMember(String memberId) {
        return loans.stream().filter(l -> l.member().memberId().equals(memberId)).toList();
    }
}
