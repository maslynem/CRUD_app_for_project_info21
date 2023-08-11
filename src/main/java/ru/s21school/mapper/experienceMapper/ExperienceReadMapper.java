package ru.s21school.mapper.experienceMapper;

import org.springframework.stereotype.Component;
import ru.s21school.dto.experienceDto.ExperienceDto;
import ru.s21school.entity.Experience;
import ru.s21school.mapper.Mapper;


@Component
public class ExperienceReadMapper implements Mapper<Experience, ExperienceDto> {


    @Override
    public ExperienceDto map(Experience object) {
        return new ExperienceDto(
                object.getId(),
                object.getCheck().getId(),
                object.getXpAmount()
        );
    }
}
