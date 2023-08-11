package ru.s21school.mapper.friendMapper;

import org.springframework.stereotype.Component;
import ru.s21school.dto.FriendDto;
import ru.s21school.entity.Friend;
import ru.s21school.mapper.Mapper;

@Component
public class FriendReadMapper implements Mapper<Friend, FriendDto> {
    @Override
    public FriendDto map(Friend object) {
        return new FriendDto(
                object.getId(),
                object.getPeer1().getNickname(),
                object.getPeer2().getNickname()
        );
    }
}
