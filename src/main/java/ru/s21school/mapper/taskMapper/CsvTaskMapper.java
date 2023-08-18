package ru.s21school.mapper.taskMapper;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import ru.s21school.dto.TaskDto;
import ru.s21school.mapper.Mapper;

@Component
public class CsvTaskMapper implements Mapper<CSVRecord, TaskDto> {
    @Override
    public TaskDto map(CSVRecord object) {
        TaskDto dto = new TaskDto();
        dto.setTitle(object.get("title"));
        dto.setParentTaskTitle(object.get("parent_task"));
        dto.setMaxXp(Long.valueOf(object.get("max_xp")));
        return dto;
    }
}
