package ru.s21school.mapper;

import org.springframework.stereotype.Component;
import ru.s21school.dto.PeerDto;
import ru.s21school.entity.Peer;

@Component
public class PeerReadMapper implements Mapper<Peer, PeerDto>{
    @Override
    public PeerDto map(Peer object) {
        return new PeerDto(object.getNickname(), object.getBirthday());
    }
}
