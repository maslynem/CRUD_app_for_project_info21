package ru.s21school.service;

import org.springframework.stereotype.Service;
import ru.s21school.dto.experienceDto.ExperienceDto;
import ru.s21school.entity.Experience;
import ru.s21school.mapper.experienceMapper.ExperienceCreateEditMapper;
import ru.s21school.mapper.experienceMapper.ExperienceReadMapper;
import ru.s21school.repository.ExperienceRepository;

@Service
public class ExperienceService extends BaseService<Experience, ExperienceDto, Long> {
    protected ExperienceService(ExperienceRepository repository, ExperienceReadMapper readMapper, ExperienceCreateEditMapper createEditMapper) {
        super(repository, readMapper, createEditMapper);
    }
}
