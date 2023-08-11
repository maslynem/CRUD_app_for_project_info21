package ru.s21school.mapper.taskMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.s21school.dto.taskDto.TaskDto;
import ru.s21school.entity.Task;
import ru.s21school.mapper.Mapper;
import ru.s21school.repository.TaskRepository;

@Component
@RequiredArgsConstructor
public class TaskCreateEditMapper implements Mapper<TaskDto, Task> {
    private final TaskRepository repository;
    @Override
    public Task map(TaskDto object) {
        return new Task(object.getTitle(), getParentTask(object), object.getMaxXp());
    }

    @Override
    public Task map(TaskDto fromObject, Task toObject) {
        toObject.setParentTask(getParentTask(fromObject));
        toObject.setMaxXp(fromObject.getMaxXp());
        return toObject;
    }

    private Task getParentTask(TaskDto taskDto) {
        String parentTaskTitle = taskDto.getParentTaskTitle();
        Task parentTask = null;
        if (parentTaskTitle != null) {
            parentTask = repository.findById(parentTaskTitle).orElse(null);
        }
        return parentTask;
    }
}
