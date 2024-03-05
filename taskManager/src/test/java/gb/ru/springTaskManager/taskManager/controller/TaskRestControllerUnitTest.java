package gb.ru.springTaskManager.taskManager.controller;

import gb.ru.springTaskManager.taskManager.model.Task;
import gb.ru.springTaskManager.taskManager.model.TaskStatus;
import gb.ru.springTaskManager.taskManager.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskRestController.class)
public class TaskRestControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @Test
    public void testListTasks() throws Exception {
        Task task = new Task();
        task.setDescription("Test Task");
        task.setStatus(TaskStatus.NOT_STARTED);
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(task.getDescription())))
                .andExpect(jsonPath("$[1].status", is(task.getStatus().toString())));
    }

    // Additional tests for other endpoints like addTask, deleteTask, etc.
}