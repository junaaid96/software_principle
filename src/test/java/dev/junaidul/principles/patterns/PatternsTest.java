package dev.junaidul.principles.patterns;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PatternsTest {
    @Test void builder() {
        var r = Creational.HttpRequest.builder("https://api.test").method("POST").header("A", "1").build();
        assertEquals("POST", r.method());
        assertThrows(IllegalStateException.class, () -> Creational.HttpRequest.builder("ftp://x").build());
    }
    @Test void factoriesAndSingletonAndPrototype() {
        assertEquals("sms:hi", Creational.notificationFor("sms").send("hi"));
        assertEquals(List.of("[dark button]", "[dark checkbox]"), Creational.renderForm(new Creational.DarkUi()));
        long a = Creational.IdGenerator.INSTANCE.nextId();
        assertEquals(a + 1, Creational.IdGenerator.INSTANCE.nextId());
        var base = new Creational.Document("t", List.of("intro"));
        assertEquals(2, base.withSection("body").sections().size());
        assertEquals(1, base.sections().size());
    }
    @Test void structural() {
        assertTrue(new Structural.LegacyPayAdapter(new Structural.LegacyPaySdk()).charge("u", 500));
        assertEquals("HI", new Structural.UpperCase(new Structural.Trimmed(new Structural.StringSource("  hi "))).read());
        assertEquals("TRACK-X1", new Structural.OrderFacade().placeOrder("u", "X1"));
        var proxy = new Structural.CachingProxy<Integer, Integer>(x -> x * x);
        proxy.apply(3); proxy.apply(3);
        assertEquals(1, proxy.realCalls);
        var root = new Structural.Folder("root").add(new Structural.FileLeaf("a", 10))
                .add(new Structural.Folder("sub").add(new Structural.FileLeaf("b", 5)));
        assertEquals(15, root.size());
    }
    @Test void behavioral() {
        assertEquals(70.0, Behavioral.BLACK_FRIDAY.apply(100), 1e-9);
        assertEquals(0.0, Behavioral.fixedOff(50).apply(20), 1e-9);
        var bus = new Behavioral.EventBus<String>();
        var seen = new ArrayList<String>();
        bus.subscribe(seen::add); bus.subscribe(e -> seen.add(e.toUpperCase()));
        bus.publish("paid");
        assertEquals(List.of("paid", "PAID"), seen);
        var ed = new Behavioral.TextEditor();
        ed.run(ed.append("Hello")); ed.run(ed.append(" World")); ed.undo();
        assertEquals("Hello", ed.text());
        assertEquals("name\na\nb", new Behavioral.CsvExporter().export(List.of("a", "b")));
        assertInstanceOf(Behavioral.Shipped.class, new Behavioral.Created().next().next());
        assertThrows(IllegalStateException.class, () -> new Behavioral.Shipped().next());
        assertEquals("manager", Behavioral.approvalChain().handle(5_000));
        assertEquals("board", Behavioral.approvalChain().handle(1_000_000));
    }
}
