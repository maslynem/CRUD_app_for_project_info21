package ru.s21school.mapper.peerToPeerMapper;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import ru.s21school.dto.PeerToPeerDto;
import ru.s21school.entity.CheckState;
import ru.s21school.mapper.Mapper;

import java.time.LocalTime;

@Component
public class CsvPeerToPeerMapper implements Mapper<CSVRecord, PeerToPeerDto> {
    @Override
    public PeerToPeerDto map(CSVRecord object) {
        PeerToPeerDto dto = new PeerToPeerDto();
        dto.setId(Long.valueOf(object.get("id")));
        dto.setCheckId(Long.valueOf(object.get("check_id")));
        dto.setCheckingPeerNickname(object.get("checking_peer"));
        dto.setCheckState(CheckState.valueOf(object.get("state")));
        dto.setTime(LocalTime.parse(object.get("time")));
        return dto;
    }
}
