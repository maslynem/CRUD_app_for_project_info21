package ru.s21school.mapper.peerMapper;

import org.springframework.stereotype.Component;
import ru.s21school.dto.peerDto.PeerDto;
import ru.s21school.entity.Peer;
import ru.s21school.mapper.Mapper;

@Component
public class PeerReadMapper implements Mapper<Peer, PeerDto> {
    @Override
    public PeerDto map(Peer object) {
        return new PeerDto(object.getNickname(), object.getBirthday());
    }
}
