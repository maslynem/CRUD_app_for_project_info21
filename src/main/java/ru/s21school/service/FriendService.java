package ru.s21school.service;

import org.springframework.stereotype.Service;
import ru.s21school.dto.FriendDto;
import ru.s21school.entity.Friend;
import ru.s21school.mapper.friendMapper.FriendCreateEditMapper;
import ru.s21school.mapper.friendMapper.FriendReadMapper;
import ru.s21school.repository.FriendRepository;

@Service
public class FriendService extends BaseService<Friend, FriendDto, Long> {
    protected FriendService(FriendRepository repository, FriendReadMapper readMapper, FriendCreateEditMapper createEditMapper) {
        super(repository, readMapper, createEditMapper);
    }
}
