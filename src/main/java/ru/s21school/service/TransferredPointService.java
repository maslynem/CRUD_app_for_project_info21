package ru.s21school.service;

import org.springframework.stereotype.Service;
import ru.s21school.dto.TransferredPointDto;
import ru.s21school.entity.TransferredPoint;
import ru.s21school.mapper.transferredPointMapper.TransferredPointCreateEditMapping;
import ru.s21school.mapper.transferredPointMapper.TransferredPointReadMapping;
import ru.s21school.repository.TransferredPointRepository;

@Service
public class TransferredPointService extends BaseService<TransferredPoint, TransferredPointDto, Long> {
    protected TransferredPointService(TransferredPointRepository repository, TransferredPointReadMapping readMapping, TransferredPointCreateEditMapping createEditMapping) {
        super(repository, readMapping, createEditMapping);
    }
}
