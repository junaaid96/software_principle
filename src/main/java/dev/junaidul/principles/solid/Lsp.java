package dev.junaidul.principles.solid;

/**
 * Liskov Substitution Principle: a subtype must be usable anywhere its base
 * type is expected without surprising the caller (same contract, no stronger
 * preconditions, no weaker postconditions).
 */
public final class Lsp {
    private Lsp() {}

    // ---------- BAD: the classic Square-extends-Rectangle trap ----------
    static class MutableRectangle {
        protected int width, height;
        void setWidth(int w) { width = w; }
        void setHeight(int h) { height = h; }
        int area() { return width * height; }
    }
    static class MutableSquare extends MutableRectangle {
        // Violates the parent's contract: setting width silently changes height.
        @Override void setWidth(int w) { width = w; height = w; }
        @Override void setHeight(int h) { width = h; height = h; }
    }
    /** Caller written against the parent contract. Breaks for MutableSquare. */
    static int stretch(MutableRectangle r) {
        r.setWidth(5);
        r.setHeight(4);
        return r.area(); // expects 20; a square returns 16
    }

    // ---------- GOOD: model only what all subtypes can honour ----------
    public interface Bird { String move(); }
    public interface FlyingBird extends Bird { String fly(); }
    public record Sparrow() implements FlyingBird {
        public String move() { return fly(); }
        public String fly() { return "sparrow flies"; }
    }
    /** Penguin is a Bird but never pretends to fly, so no UnsupportedOperationException. */
    public record Penguin() implements Bird {
        public String move() { return "penguin swims"; }
    }

    public static void main(String[] args) {
        System.out.println(stretch(new MutableRectangle())); // 20
        System.out.println(stretch(new MutableSquare()));    // 16, the LSP violation
        for (Bird b : new Bird[]{new Sparrow(), new Penguin()}) System.out.println(b.move());
    }
}
