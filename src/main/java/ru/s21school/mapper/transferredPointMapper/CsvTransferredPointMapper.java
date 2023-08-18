package ru.s21school.mapper.transferredPointMapper;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import ru.s21school.dto.TransferredPointDto;
import ru.s21school.mapper.Mapper;

@Component
public class CsvTransferredPointMapper implements Mapper<CSVRecord, TransferredPointDto> {
    @Override
    public TransferredPointDto map(CSVRecord object) {
        TransferredPointDto dto = new TransferredPointDto();
        dto.setId(Long.valueOf(object.get("id")));
        dto.setPointsAmount(Long.valueOf(object.get("points_amount")));
        dto.setCheckedPeerNickname(object.get("checked_peer"));
        dto.setCheckingPeerNickname(object.get("checking_peer"));
        return dto;
    }
}
