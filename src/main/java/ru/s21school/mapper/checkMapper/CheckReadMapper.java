package ru.s21school.mapper.checkMapper;

import org.springframework.stereotype.Component;
import ru.s21school.dto.checkDto.CheckDto;
import ru.s21school.entity.Check;
import ru.s21school.mapper.Mapper;


@Component
public class CheckReadMapper implements Mapper<Check, CheckDto> {
    @Override
    public CheckDto map(Check object) {
        return new CheckDto(
                object.getId(),
                object.getPeer().getNickname(),
                object.getTask().getTitle(),
                object.getDate()
        );
    }

}
