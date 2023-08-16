package ru.s21school.mapper.transferredPointMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.s21school.dto.TransferredPointDto;
import ru.s21school.entity.Peer;
import ru.s21school.entity.TransferredPoint;
import ru.s21school.exceptions.NoSuchPeerException;
import ru.s21school.mapper.Mapper;
import ru.s21school.repository.PeerRepository;

@Component
@RequiredArgsConstructor
public class TransferredPointCreateEditMapping implements Mapper<TransferredPointDto, TransferredPoint> {
    private final PeerRepository peerRepository;

    @Override
    public TransferredPoint map(TransferredPointDto object) {
        Peer checkingPeer = peerRepository
                .findById(object.getCheckingPeerNickname())
                .orElseThrow(() -> new NoSuchPeerException("No peer with nickname " + object.getCheckingPeerNickname()));

        Peer checkedPeer = peerRepository
                .findById(object.getCheckedPeerNickname())
                .orElseThrow(() -> new NoSuchPeerException("No peer with nickname " + object.getCheckedPeerNickname()));

        return new TransferredPoint(
                object.getId(),
                checkingPeer,
                checkedPeer,
                object.getPointsAmount()
        );
    }

    @Override
    public TransferredPoint map(TransferredPointDto fromObject, TransferredPoint toObject) {
        toObject.setPointsAmount(fromObject.getPointsAmount());
        return toObject;
    }
}
