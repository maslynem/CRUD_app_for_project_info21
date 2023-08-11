package ru.s21school.service;

import org.springframework.stereotype.Service;
import ru.s21school.dto.checkDto.CheckDto;
import ru.s21school.entity.Check;
import ru.s21school.mapper.checkMapper.CheckCreateEditMapper;
import ru.s21school.mapper.checkMapper.CheckReadMapper;
import ru.s21school.repository.CheckRepository;

@Service
public class VerterService extends BaseService<Check, CheckDto, Long> {
    protected VerterService(CheckRepository repository, CheckReadMapper checkReadMapper, CheckCreateEditMapper createEditMapper) {
        super(repository, checkReadMapper, createEditMapper);
    }
}
