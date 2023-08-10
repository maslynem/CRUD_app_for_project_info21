package ru.s21school.util.validators.taskValidators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.s21school.dto.taskDto.TaskDto;
import ru.s21school.service.TaskService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskSaveValidator implements Validator {
    private final TaskService taskService;

    @Override
    public boolean supports(Class<?> clazz) {
        return TaskDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TaskDto task = (TaskDto) target;
        Optional<TaskDto> byTitle = taskService.findById(task.getTitle());
        if (byTitle.isPresent()) {
            errors.rejectValue("title", "", "Title is already taken");
        }
        String parentTaskTitle = task.getParentTaskTitle();
        if (parentTaskTitle != null) {
            Optional<TaskDto> parentTask = taskService.findById(parentTaskTitle);
            if (!parentTask.isPresent()) {
                errors.rejectValue("parentTask", "", "Task with this title does not exist");
            }
        }
    }
}
