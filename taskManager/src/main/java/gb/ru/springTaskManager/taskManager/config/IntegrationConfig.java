package gb.ru.springTaskManager.taskManager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.io.File;

@Configuration
@EnableIntegration
public class IntegrationConfig {

    @Bean
    public MessageChannel taskChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "taskChannel")
    public MessageHandler fileWriter() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File("tasks"));
        handler.setExpectReply(false);
        handler.setFileExistsMode(FileExistsMode.APPEND);
        handler.setAppendNewLine(true);
        handler.setFileNameGenerator(message -> {
            // Генерация имени файла на основе текущего времени, можно настроить по своему усмотрению
            return "task-" + System.currentTimeMillis() + ".txt";
        });
        return handler;
    }
}
