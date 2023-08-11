package ru.s21school.mapper.peerToPeerMapper;

import org.springframework.stereotype.Component;
import ru.s21school.dto.peerToPeerDto.PeerToPeerDto;
import ru.s21school.entity.PeerToPeer;
import ru.s21school.mapper.Mapper;

@Component
public class PeerToPeerReadMapper implements Mapper<PeerToPeer, PeerToPeerDto> {
    @Override
    public PeerToPeerDto map(PeerToPeer object) {
        return new PeerToPeerDto(
                object.getId(),
                object.getCheck().getId(),
                object.getCheckingPeer().getNickname(),
                object.getState(),
                object.getTime()
        );
    }
}
