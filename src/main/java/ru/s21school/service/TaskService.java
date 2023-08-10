package ru.s21school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.s21school.dto.taskDto.TaskDto;
import ru.s21school.entity.Task;
import ru.s21school.mapper.Mapper;
import ru.s21school.mapper.taskMapper.TaskCreateEditMapper;
import ru.s21school.mapper.taskMapper.TaskReadMapper;
import ru.s21school.repository.TaskRepository;

@Service
@Transactional(readOnly = true)
public class TaskService extends BaseService<Task, TaskDto, String> {


    protected TaskService(TaskRepository repository, TaskReadMapper taskReadMapper, TaskCreateEditMapper createEditMapper) {
        super(repository, taskReadMapper, createEditMapper);
    }
}
