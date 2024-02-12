package gb.ru.springTaskManager.taskManager.controller;

import gb.ru.springTaskManager.taskManager.model.Task;
import gb.ru.springTaskManager.taskManager.model.TaskStatus;
import gb.ru.springTaskManager.taskManager.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TaskController {

    private final TaskRepository taskRepository;

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
    public String addTask(@ModelAttribute Task task) {
        taskRepository.save(task);
        return "redirect:/"; // Redirect to task list after adding a task
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
        taskRepository.save(task); // Assumes task.id is already set
        return "redirect:/"; // Redirect to task list after updating a task
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        taskRepository.deleteById(id);
        return "redirect:/"; // Redirect back to the task list
    }
}
