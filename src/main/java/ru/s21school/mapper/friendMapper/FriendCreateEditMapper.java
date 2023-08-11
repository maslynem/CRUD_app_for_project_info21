package ru.s21school.mapper.friendMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.s21school.dto.FriendDto;
import ru.s21school.entity.Friend;
import ru.s21school.entity.Peer;
import ru.s21school.mapper.Mapper;
import ru.s21school.repository.PeerRepository;

@Component
@RequiredArgsConstructor
public class FriendCreateEditMapper implements Mapper<FriendDto, Friend> {
    private final PeerRepository peerRepository;

    @Override
    public Friend map(FriendDto object) {
        Peer peer1 = peerRepository.findById(object.getPeerOneNickname()).orElse(null);
        Peer peer2 = peerRepository.findById(object.getPeerTwoNickname()).orElse(null);
        return new Friend(
                object.getId(),
                peer1,
                peer2
        );
    }

    @Override
    public Friend map(FriendDto fromObject, Friend toObject) {
        Peer peer1 = peerRepository.findById(fromObject.getPeerOneNickname()).orElse(null);
        Peer peer2 = peerRepository.findById(fromObject.getPeerTwoNickname()).orElse(null);
        toObject.setPeer1(peer1);
        toObject.setPeer2(peer2);
        return toObject;
    }
}
