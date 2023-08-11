package ru.s21school.mapper.verterMapper;

import org.springframework.stereotype.Component;
import ru.s21school.dto.verterDto.VerterDto;
import ru.s21school.entity.Verter;
import ru.s21school.mapper.Mapper;

@Component
public class VerterReadMapper implements Mapper<Verter, VerterDto> {
    @Override
    public VerterDto map(Verter object) {
        return new VerterDto(
                object.getId(),
                object.getCheck().getId(),
                object.getState(),
                object.getTime()
        );
    }
}
