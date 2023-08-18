package ru.s21school.mapper.checkMapper;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import ru.s21school.dto.CheckDto;
import ru.s21school.mapper.Mapper;

import java.time.LocalDate;

@Component
public class CsvToCheckMapper implements Mapper<CSVRecord, CheckDto> {
    @Override
    public CheckDto map(CSVRecord object) {
        CheckDto dto = new CheckDto();
        dto.setId(Long.valueOf(object.get("id")));
        dto.setPeerNickname(object.get("peer"));
        dto.setTaskTitle(object.get("task"));
        dto.setDate(LocalDate.parse(object.get("date")));
        return dto;
    }
}
