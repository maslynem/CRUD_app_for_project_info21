package ru.s21school.service;

import org.springframework.stereotype.Service;
import ru.s21school.dto.TaskDto;
import ru.s21school.entity.Task;
import ru.s21school.mapper.taskMapper.TaskCreateEditMapper;
import ru.s21school.mapper.taskMapper.TaskReadMapper;
import ru.s21school.repository.TaskRepository;

@Service
public class TaskService extends BaseService<Task, TaskDto, String> {


    protected TaskService(TaskRepository repository, TaskReadMapper taskReadMapper, TaskCreateEditMapper createEditMapper) {
        super(repository, taskReadMapper, createEditMapper);
    }
}
