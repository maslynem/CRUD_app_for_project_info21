package ru.s21school.mapper.timeTrackingMapper;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import ru.s21school.dto.TimeTrackingDto;
import ru.s21school.mapper.Mapper;

import java.time.LocalDate;
import java.time.LocalTime;

import static ru.s21school.dto.TimeTrackingDto.State.IN;
import static ru.s21school.dto.TimeTrackingDto.State.OUT;

@Component
public class CsvTimeTrackingMapper implements Mapper<CSVRecord, TimeTrackingDto> {
    @Override
    public TimeTrackingDto map(CSVRecord object) {
        TimeTrackingDto dto = new TimeTrackingDto();
        dto.setId(Long.valueOf(object.get("id")));
        dto.setPeerNickname(object.get("peer"));
        dto.setDate(LocalDate.parse(object.get("date")));
        dto.setTime(LocalTime.parse(object.get("time")));
        dto.setState("1".equals(object.get("state")) ? IN : OUT);
        return dto;
    }
}
