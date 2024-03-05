package gb.ru.springTaskManager.taskManager.repositories;

import gb.ru.springTaskManager.taskManager.model.Task;
import gb.ru.springTaskManager.taskManager.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testFindByStatus() {
        // Setup our mock repository
        Task task = new Task();
        task.setDescription("Test Task");
        task.setStatus(TaskStatus.NOT_STARTED);
        taskRepository.save(task);

        // Execute the service call
        List<Task> foundTasks = taskRepository.findByStatus(TaskStatus.NOT_STARTED);

        // Assert the response
        assertEquals(1, foundTasks.size(), "Should find one task");
    }

}
