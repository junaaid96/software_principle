package dev.junaidul.principles.ooad.library;

import java.util.List;
import java.util.Optional;

/** Port for persistence, so LibraryService stays independent of any database. */
public interface LoanRepository {
    void save(Loan loan);
    void remove(Loan loan);
    Optional<Loan> findActiveByCopy(String copyId);
    List<Loan> findActiveByMember(String memberId);
}
