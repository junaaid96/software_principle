package dev.junaidul.principles.solid;

/**
 * Interface Segregation Principle: clients should not depend on methods they do
 * not use. Prefer several small role interfaces over one fat interface.
 */
public final class Isp {
    private Isp() {}

    // ---------- BAD: a fat interface forces dummy implementations ----------
    interface MultiFunctionDevice {
        String print(String doc);
        String scan(String doc);
        String fax(String doc);
    }
    static final class CheapPrinter implements MultiFunctionDevice {
        public String print(String doc) { return "printed " + doc; }
        public String scan(String doc) { throw new UnsupportedOperationException(); } // smell
        public String fax(String doc) { throw new UnsupportedOperationException(); }  // smell
    }

    // ---------- GOOD: small role interfaces, combine only what you support ----------
    public interface Printer { String print(String doc); }
    public interface Scanner { String scan(String doc); }

    public static final class SimplePrinter implements Printer {
        public String print(String doc) { return "printed " + doc; }
    }
    public static final class OfficeMachine implements Printer, Scanner {
        public String print(String doc) { return "printed " + doc; }
        public String scan(String doc) { return "scanned " + doc; }
    }

    /** This client depends only on Printer, so it accepts both machines. */
    public static String printReport(Printer printer) { return printer.print("report"); }

    public static void main(String[] args) {
        System.out.println(printReport(new SimplePrinter()));
        System.out.println(printReport(new OfficeMachine()));
        System.out.println(new CheapPrinter().print("legacy"));
    }
}
