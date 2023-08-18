package ru.s21school.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.s21school.dto.TaskDto;
import ru.s21school.entity.Task;
import ru.s21school.mapper.taskMapper.CsvTaskMapper;
import ru.s21school.mapper.taskMapper.TaskCreateEditMapper;
import ru.s21school.mapper.taskMapper.TaskReadMapper;
import ru.s21school.repository.TaskRepository;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskService extends BaseService<Task, TaskDto, String> {


    protected TaskService(TaskRepository repository, TaskReadMapper taskReadMapper, TaskCreateEditMapper createEditMapper, CsvTaskMapper csvMapper) {
        super(repository, taskReadMapper, createEditMapper, csvMapper);
    }

    public List<TaskDto> findAll() {
        Sort sort = Sort.by("title").ascending();
        return repository.findAll(sort).stream().map(tdMapper::map).collect(Collectors.toList());
    }

    public void writeToCsv(Writer writer) {
        List<Task> list = repository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("title", "parent_task", "max_xp");
            for (Task entity : list) {
                csvPrinter.printRecord(
                        entity.getTitle(),
                        Optional.ofNullable(entity.getParentTask()).orElse(new Task()).getTitle(),
                        entity.getMaxXp()
                );
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }

}
