package ru.s21school.mapper.peerMapper;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import ru.s21school.dto.PeerDto;
import ru.s21school.mapper.Mapper;

import java.time.LocalDate;

@Component
public class CsvToPeerMapper implements Mapper<CSVRecord, PeerDto> {
    @Override
    public PeerDto map(CSVRecord object) {
        PeerDto dto = new PeerDto();
        dto.setNickname(object.get("Nickname"));
        dto.setBirthday(LocalDate.parse(object.get("Birthday")));
        return dto;
    }
}
