package dev.junaidul.principles.architecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Hexagonal (ports and adapters) + CQRS + domain events in one small example.
 * Dependency rule: adapters -> application -> domain. The domain imports nothing outward.
 */
public final class Hexagonal {
    private Hexagonal() {}

    // ===== Domain (innermost): pure business rules =====
    public record Task(String id, String title, boolean done) {
        public Task complete() {
            if (done) throw new IllegalStateException("already done");
            return new Task(id, title, true);       // immutable: return a new state
        }
    }
    /** Domain event: a fact that happened, named in past tense. */
    public record TaskCompleted(String taskId) {}

    // ===== Ports (owned by the application core) =====
    /** Driving (inbound) port: what the outside world can ask us to do. Commands. */
    public interface TaskCommands { String create(String title); void complete(String id); }
    /** Query side of CQRS: read-optimized, never changes state. */
    public interface TaskQueries { List<String> openTitles(); }
    /** Driven (outbound) ports: what the core needs from the outside world. */
    public interface TaskRepository { void save(Task t); Optional<Task> find(String id); List<Task> all(); }
    public interface EventPublisher { void publish(Object event); }

    // ===== Application service: orchestrates use cases, no I/O details =====
    public static final class TaskService implements TaskCommands, TaskQueries {
        private final TaskRepository repo;
        private final EventPublisher events;
        private int seq;
        public TaskService(TaskRepository repo, EventPublisher events) { this.repo = repo; this.events = events; }

        public String create(String title) {
            if (title == null || title.isBlank()) throw new IllegalArgumentException("title required");
            var t = new Task("T" + (++seq), title, false);
            repo.save(t);
            return t.id();
        }
        public void complete(String id) {
            var t = repo.find(id).orElseThrow(() -> new IllegalArgumentException("not found " + id));
            repo.save(t.complete());                     // domain decides if allowed
            events.publish(new TaskCompleted(id));       // event-driven: others react later
        }
        public List<String> openTitles() {
            return repo.all().stream().filter(t -> !t.done()).map(Task::title).toList();
        }
    }

    // ===== Adapters (outermost): technology details, easy to replace =====
    public static final class InMemoryTaskRepository implements TaskRepository {
        private final Map<String, Task> rows = new HashMap<>();
        public void save(Task t) { rows.put(t.id(), t); }
        public Optional<Task> find(String id) { return Optional.ofNullable(rows.get(id)); }
        public List<Task> all() { return List.copyOf(rows.values()); }
    }
    public static final class InMemoryEventPublisher implements EventPublisher {
        private final List<Consumer<Object>> listeners = new ArrayList<>();
        public void subscribe(Consumer<Object> l) { listeners.add(l); }
        public void publish(Object e) { listeners.forEach(l -> l.accept(e)); }
    }
    /** A "controller" adapter: translates an HTTP-like request into a port call. */
    public record CliController(TaskCommands commands) {
        public String handle(String line) {
            var parts = line.split(" ", 2);
            return switch (parts[0]) {
                case "add" -> "created " + commands.create(parts[1]);
                case "done" -> { commands.complete(parts[1]); yield "ok"; }
                default -> "unknown command";
            };
        }
    }
}
