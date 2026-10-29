package dev.junaidul.principles.refactoring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Characterization test: the refactored code must match the legacy output exactly. */
class RefactoringTest {
    @ParameterizedTest
    @CsvSource({"0,1", "0,2", "0,5", "1,1", "1,4", "2,1", "2,3", "2,7"})
    void behaviourIsPreserved(int type, int days) {
        var category = Refactoring.Category.values()[type];
        assertEquals(Refactoring.before(type, days), Refactoring.after(List.of(new Refactoring.Rental(category, days))));
    }
}
