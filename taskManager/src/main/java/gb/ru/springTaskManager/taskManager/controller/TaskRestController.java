package gb.ru.springTaskManager.taskManager.controller;

import gb.ru.springTaskManager.taskManager.model.Task;
import gb.ru.springTaskManager.taskManager.repositories.TaskRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks") // Base URI for all controller methods
public class TaskRestController {

    private final TaskRepository taskRepository;
    private final Counter requestCounter = Metrics.counter("add_note_count");

    @Autowired
    public TaskRestController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // List all tasks
    @GetMapping
    public List<Task> listTasks() {
        return taskRepository.findAll();
    }

    // Get a single task by ID
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable Long taskId) {
        return taskRepository.findById(taskId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Add a new task with validation
    @PostMapping
    public ResponseEntity<Task> addTask(@Valid @RequestBody Task task) {
        Task savedTask = taskRepository.save(task);
        requestCounter.increment();
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    // Update an existing task
    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @Valid @RequestBody Task taskDetails) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    task.setDescription(taskDetails.getDescription());
                    task.setStatus(taskDetails.getStatus());
                    // Update other fields as necessary
                    Task updatedTask = taskRepository.save(task);
                    requestCounter.increment();
                    return ResponseEntity.ok(updatedTask);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a task
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.deleteById(taskId);
        requestCounter.increment();
        return ResponseEntity.ok().build();
    }
}
