package ru.s21school.mapper.timeTrackingMapper;

import org.springframework.stereotype.Component;
import ru.s21school.dto.TimeTrackingDto;
import ru.s21school.entity.TimeTracking;
import ru.s21school.mapper.Mapper;

import static ru.s21school.dto.TimeTrackingDto.State.IN;
import static ru.s21school.dto.TimeTrackingDto.State.OUT;

@Component
public class TimeTrackingReadMapper implements Mapper<TimeTracking, TimeTrackingDto> {
    @Override
    public TimeTrackingDto map(TimeTracking object) {
        return new TimeTrackingDto(
                object.getId(),
                object.getPeer().getNickname(),
                object.getDate(),
                object.getTime(),
                object.getState() == 1 ? IN : OUT
        );
    }
}
