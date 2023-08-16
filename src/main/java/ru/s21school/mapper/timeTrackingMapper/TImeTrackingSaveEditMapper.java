package ru.s21school.mapper.timeTrackingMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.s21school.dto.TimeTrackingDto;
import ru.s21school.entity.Peer;
import ru.s21school.entity.TimeTracking;
import ru.s21school.exceptions.NoSuchPeerException;
import ru.s21school.mapper.Mapper;
import ru.s21school.repository.PeerRepository;

import static ru.s21school.dto.TimeTrackingDto.State.IN;

@Component
@RequiredArgsConstructor
public class TImeTrackingSaveEditMapper implements Mapper<TimeTrackingDto, TimeTracking> {

    private final PeerRepository peerRepository;

    @Override
    public TimeTracking map(TimeTrackingDto object) {

        Peer peer = peerRepository
                .findById(object.getPeerNickname())
                .orElseThrow(() -> new NoSuchPeerException("Peer with this nickname does not exist: " + object.getPeerNickname()));

        return new TimeTracking(
                object.getId(),
                peer,
                object.getDate(),
                object.getTime(),
                object.getState() == IN ? 1 : 2
        );
    }

    @Override
    public TimeTracking map(TimeTrackingDto fromObject, TimeTracking toObject) {
        Peer peer = peerRepository
                .findById(fromObject.getPeerNickname())
                .orElseThrow(() -> new NoSuchPeerException("Peer with this nickname does not exist: " + fromObject.getPeerNickname()));

        toObject.setPeer(peer);
        toObject.setDate(fromObject.getDate());
        toObject.setTime(fromObject.getTime());
        toObject.setState(fromObject.getState().ordinal() + 1);

        return toObject;
    }
}
