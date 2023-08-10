package ru.s21school.mapper.peerMapper;

import org.springframework.stereotype.Component;
import ru.s21school.dto.peerDto.PeerDto;
import ru.s21school.entity.Peer;
import ru.s21school.mapper.Mapper;

@Component
public class PeerCreateEditMapper implements Mapper<PeerDto, Peer> {
    @Override
    public Peer map(PeerDto object) {
        return new Peer(object.getNickname(), object.getBirthday());
    }

    @Override
    public Peer map(PeerDto fromObject, Peer toObject) {
        toObject.setBirthday(fromObject.getBirthday());
        return toObject;
    }
}
