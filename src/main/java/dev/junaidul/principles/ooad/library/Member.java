package dev.junaidul.principles.ooad.library;

/** A library member. maxLoans comes from the requirement "members borrow up to N books". */
public record Member(String memberId, String name, int maxLoans) {
    public Member {
        if (maxLoans <= 0) throw new IllegalArgumentException("maxLoans must be positive");
    }
}
