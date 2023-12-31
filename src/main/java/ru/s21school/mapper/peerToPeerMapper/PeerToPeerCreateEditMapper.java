package ru.s21school.mapper.peerToPeerMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.s21school.dto.PeerToPeerDto;
import ru.s21school.entity.Check;
import ru.s21school.entity.Peer;
import ru.s21school.entity.PeerToPeer;
import ru.s21school.exceptions.NoSuchCheckException;
import ru.s21school.exceptions.NoSuchPeerException;
import ru.s21school.mapper.Mapper;
import ru.s21school.repository.CheckRepository;
import ru.s21school.repository.PeerRepository;

@Component
@RequiredArgsConstructor
public class PeerToPeerCreateEditMapper implements Mapper<PeerToPeerDto, PeerToPeer> {
    private final CheckRepository checkRepository;
    private final PeerRepository peerRepository;

    @Override
    public PeerToPeer map(PeerToPeerDto object) {
        Check check = checkRepository
                .findById(object.getCheckId())
                .orElseThrow(() -> new NoSuchCheckException("Check with this id does not exist: " + object.getCheckId()));
        Peer peer = peerRepository
                .findById(object.getCheckingPeerNickname())
                .orElseThrow(() -> new NoSuchPeerException("Peer with this nickname does not exist: " + object.getCheckingPeerNickname()));

        return new PeerToPeer(
                object.getId(),
                check,
                peer,
                object.getCheckState(),
                object.getTime()
        );
    }

    @Override
    public PeerToPeer map(PeerToPeerDto fromObject, PeerToPeer toObject) {
        toObject.setState(fromObject.getCheckState());
        toObject.setTime(fromObject.getTime());
        return toObject;
    }
}
