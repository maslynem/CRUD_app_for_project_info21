package ru.s21school.service;

import org.springframework.stereotype.Service;
import ru.s21school.dto.checkDto.CheckDto;
import ru.s21school.entity.Check;
import ru.s21school.mapper.checkMapper.CheckCreateEditMapper;
import ru.s21school.mapper.checkMapper.CheckReadMapper;
import ru.s21school.repository.CheckRepository;

@Service
public class CheckService extends BaseService<Check, CheckDto, Long> {

    protected CheckService(CheckRepository repository, CheckReadMapper checkReadMapper, CheckCreateEditMapper createEditMapper) {
        super(repository, checkReadMapper, createEditMapper);
    }
}
