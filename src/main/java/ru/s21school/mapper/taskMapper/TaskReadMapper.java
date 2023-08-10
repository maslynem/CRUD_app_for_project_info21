package ru.s21school.mapper.taskMapper;

import org.springframework.stereotype.Component;
import ru.s21school.dto.taskDto.TaskDto;
import ru.s21school.entity.Task;
import ru.s21school.mapper.Mapper;

@Component
public class TaskReadMapper implements Mapper<Task, TaskDto> {
    @Override
    public TaskDto map(Task object) {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle(object.getTitle());
        taskDto.setMaxXp(object.getMaxXp());
        if (object.getParentTask() == null) {
            taskDto.setParentTaskTitle(null);
        } else {
            taskDto.setParentTaskTitle(object.getParentTask().getTitle());
        }
        return taskDto;
    }
}
