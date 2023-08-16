package ru.s21school.service;

import org.springframework.stereotype.Service;
import ru.s21school.dto.VerterDto;
import ru.s21school.entity.Verter;
import ru.s21school.mapper.verterMapper.VerterCreateEditMapper;
import ru.s21school.mapper.verterMapper.VerterReadMapper;
import ru.s21school.repository.VerterRepository;

@Service
public class VerterService extends BaseService<Verter, VerterDto, Long> {
    protected VerterService(VerterRepository repository, VerterReadMapper checkReadMapper, VerterCreateEditMapper createEditMapper) {
        super(repository, checkReadMapper, createEditMapper);
    }
}
