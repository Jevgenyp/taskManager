package gb.ru.springTaskManager.taskManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"gb.ru.springTaskManager.taskManager.model"})
@EnableJpaRepositories(basePackages = {"gb.ru.springTaskManager.taskManager.repositories"})
public class TaskManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskManagerApplication.class, args);
	}
}
