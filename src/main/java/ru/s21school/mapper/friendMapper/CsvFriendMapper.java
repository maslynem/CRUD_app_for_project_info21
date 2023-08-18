package ru.s21school.mapper.friendMapper;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import ru.s21school.dto.FriendDto;
import ru.s21school.mapper.Mapper;

@Component
public class CsvFriendMapper implements Mapper<CSVRecord, FriendDto> {
    @Override
    public FriendDto map(CSVRecord object) {
        FriendDto friendDto = new FriendDto();
        friendDto.setId(Long.valueOf(object.get("id")));
        friendDto.setPeerOneNickname(object.get("peer1"));
        friendDto.setPeerTwoNickname(object.get("peer2"));
        return friendDto;
    }
}
