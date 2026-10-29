package dev.junaidul.principles.solid;

import java.util.List;

/**
 * Open/Closed Principle: open for extension, closed for modification.
 * Add new behaviour by adding new code, not by editing tested code.
 */
public final class Ocp {
    private Ocp() {}

    // ---------- BAD: every new shape forces an edit to this if-chain ----------
    static double badArea(Object shape) {
        if (shape instanceof double[] r && r.length == 2) return r[0] * r[1]; // "rectangle"
        if (shape instanceof Double radius) return Math.PI * radius * radius; // "circle"
        throw new IllegalArgumentException("unknown shape"); // new shape = modify here
    }

    // ---------- GOOD: an abstraction; each shape knows its own area ----------
    public interface Shape { double area(); }
    public record Circle(double radius) implements Shape {
        public double area() { return Math.PI * radius * radius; }
    }
    public record Rectangle(double width, double height) implements Shape {
        public double area() { return width * height; }
    }
    /** Added later without touching totalArea(): that is OCP. */
    public record Square(double side) implements Shape {
        public double area() { return side * side; }
    }

    /** Closed for modification: works for any present or future Shape. */
    public static double totalArea(List<? extends Shape> shapes) {
        return shapes.stream().mapToDouble(Shape::area).sum();
    }

    public static void main(String[] args) {
        System.out.println(totalArea(List.of(new Circle(1), new Rectangle(2, 3), new Square(2))));
        System.out.println(badArea(2.0)); // shown only for comparison
    }
}
