package ru.s21school.mapper.verterMapper;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import ru.s21school.dto.VerterDto;
import ru.s21school.entity.CheckState;
import ru.s21school.mapper.Mapper;

import java.time.LocalTime;

@Component
public class CsvVerterMapper implements Mapper<CSVRecord, VerterDto> {
    @Override
    public VerterDto map(CSVRecord object) {
        VerterDto dto = new VerterDto();
        dto.setId(Long.valueOf(object.get("id")));
        dto.setTime(LocalTime.parse(object.get("time")));
        dto.setCheckState(CheckState.valueOf(object.get("state")));
        dto.setCheckId(Long.valueOf(object.get("check_id")));
        return dto;
    }
}
