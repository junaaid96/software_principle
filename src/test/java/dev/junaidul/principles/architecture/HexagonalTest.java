package dev.junaidul.principles.architecture;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class HexagonalTest {
    @Test void commandsQueriesAndEvents() {
        var events = new Hexagonal.InMemoryEventPublisher();
        var received = new ArrayList<Object>();
        events.subscribe(received::add);
        var service = new Hexagonal.TaskService(new Hexagonal.InMemoryTaskRepository(), events);
        var cli = new Hexagonal.CliController(service);

        assertEquals("created T1", cli.handle("add write ADR"));
        cli.handle("add review PR");
        assertEquals("ok", cli.handle("done T1"));
        assertEquals(List.of("review PR"), service.openTitles());
        assertEquals(List.of(new Hexagonal.TaskCompleted("T1")), received);
        assertThrows(IllegalStateException.class, () -> service.complete("T1"));
    }
}
