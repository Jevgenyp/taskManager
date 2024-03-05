package gb.ru.springTaskManager.taskManager.controller;

import gb.ru.springTaskManager.taskManager.aspects.TrackUserAction;
import gb.ru.springTaskManager.taskManager.model.Task;
import gb.ru.springTaskManager.taskManager.repositories.TaskRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TaskController {

    private final TaskRepository taskRepository;
    private final Counter requestCounter = Metrics.counter("add_note_count");

    @Autowired
    private MessageChannel taskChannel;


    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/")
    public String listTasks(Model model) {
        model.addAttribute("tasks", taskRepository.findAll());
        return "tasks"; // HTML template for listing tasks
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Task task = new Task();
        model.addAttribute("task", task);
        return "add-task"; // HTML form for adding a new task
    }

    @PostMapping("/add")
    @TrackUserAction // Аннотация для AOP
    public String addTask(@ModelAttribute Task task) {
        taskRepository.save(task);
        requestCounter.increment();

        // Конвертация задачи в строку (или другой формат по вашему усмотрению) и отправка
        String taskInfo = convertTaskToString(task);
        Message<String> message = MessageBuilder.withPayload(taskInfo).build();
        taskChannel.send(message);

        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        model.addAttribute("task", task);
        return "edit-task"; // View name for the edit form
    }

    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable("id") Long id, @ModelAttribute("task") Task task) {
        taskRepository.save(task);
        requestCounter.increment();

        // Повторно используем логику отправки данных в канал
        String taskInfo = convertTaskToString(task);
        Message<String> message = MessageBuilder.withPayload(taskInfo).build();
        taskChannel.send(message);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        taskRepository.deleteById(id);
        return "redirect:/"; // Redirect back to the task list
    }
    private String convertTaskToString(Task task) {
        return "Task ID: " + task.getId() + ", Description: " + task.getDescription() + ", Status: " + task.getStatus();
    }
}
