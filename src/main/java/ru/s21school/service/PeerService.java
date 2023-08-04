package ru.s21school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.s21school.dto.PeerDto;
import ru.s21school.entity.Peer;
import ru.s21school.mapper.PeerReadMapper;
import ru.s21school.mapper.PeerUpdateMapper;
import ru.s21school.repository.PeerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PeerService {
    private final PeerRepository peerRepository;
    private final PeerReadMapper peerReadMapper;
    private final PeerUpdateMapper peerUpdateMapper;

    public List<PeerDto> findAll() {
        return peerRepository.findAll().stream()
                .map(peerReadMapper::map)
                .collect(Collectors.toList());
    }

    public PeerDto findByNickname(String nickname) {
        Optional<Peer> byId = peerRepository.findById(nickname);
        return byId.map(peerReadMapper::map).orElse(null);
    }

    public void update(PeerDto peerUpdate) {
        Optional<Peer> byId = peerRepository.findById(peerUpdate.getNickname());
        byId.get().setBirthday(peerUpdate.getBirthday());
        peerRepository.flush();
    }
}
