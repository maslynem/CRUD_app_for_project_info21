package ru.s21school.service;

import org.springframework.stereotype.Service;
import ru.s21school.dto.TimeTrackingDto;
import ru.s21school.entity.TimeTracking;
import ru.s21school.mapper.timeTrackingMapper.TImeTrackingSaveEditMapper;
import ru.s21school.mapper.timeTrackingMapper.TimeTrackingReadMapper;
import ru.s21school.repository.TimeTrackingRepository;

@Service
public class TimeTrackingService extends BaseService<TimeTracking, TimeTrackingDto, Long> {
    protected TimeTrackingService(TimeTrackingRepository repository, TimeTrackingReadMapper readMapper, TImeTrackingSaveEditMapper saveEditMapper) {
        super(repository, readMapper, saveEditMapper);
    }
}
