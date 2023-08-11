package ru.s21school.mapper.transferredPointMapper;

import org.springframework.stereotype.Component;
import ru.s21school.dto.TransferredPointDto;
import ru.s21school.entity.TransferredPoint;
import ru.s21school.mapper.Mapper;

@Component
public class TransferredPointReadMapping implements Mapper<TransferredPoint, TransferredPointDto> {
    @Override
    public TransferredPointDto map(TransferredPoint object) {
        return new TransferredPointDto(
                object.getId(),
                object.getCheckingPeer().getNickname(),
                object.getCheckedPeer().getNickname(),
                object.getPointsAmount()
        );
    }
}
