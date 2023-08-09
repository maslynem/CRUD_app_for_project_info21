package ru.s21school.mapper;

import org.springframework.stereotype.Component;
import ru.s21school.dto.PeerDto;
import ru.s21school.entity.Peer;

@Component
public class PeerSaveMapper implements Mapper<PeerDto, Peer> {
    @Override
    public Peer map(PeerDto object) {
        return new Peer(
                object.getNickname(),
                object.getBirthday()
        );
    }
}
