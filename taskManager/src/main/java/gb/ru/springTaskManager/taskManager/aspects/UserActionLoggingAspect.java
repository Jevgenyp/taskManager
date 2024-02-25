package gb.ru.springTaskManager.taskManager.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Aspect
@Component
public class UserActionLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(UserActionLoggingAspect.class);
    @Pointcut("@annotation(gb.ru.springTaskManager.taskManager.aspects.TrackUserAction)")
    public void trackUserAction() {}
    @After("trackUserAction()")
    public void afterCallingTrackedMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Object[] args = joinPoint.getArgs();

        // Проверка и обработка аргументов метода
        for (Object arg : args) {
            if (arg instanceof gb.ru.springTaskManager.taskManager.model.Task) {
                gb.ru.springTaskManager.taskManager.model.Task task = (gb.ru.springTaskManager.taskManager.model.Task) arg;
                //  логирование деталей задачи
                logger.info("Task added: description={}, status={}, createdAt={}",
                        task.getDescription(), task.getStatus(), task.getCreatedAt());
            }
        }
    }


//
//    @After("trackUserAction()")
//    public void afterCallingTrackedMethod(JoinPoint joinPoint) {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        String methodName = signature.getMethod().getName();
//        Object[] args = joinPoint.getArgs();
//
//        System.out.println("User action tracked: " + methodName + ", args: " + java.util.Arrays.toString(args));
//    }
}

