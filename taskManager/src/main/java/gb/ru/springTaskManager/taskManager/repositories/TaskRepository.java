package gb.ru.springTaskManager.taskManager.repositories;

import gb.ru.springTaskManager.taskManager.model.Task;
import gb.ru.springTaskManager.taskManager.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
}
