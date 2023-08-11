package ru.s21school.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.s21school.dto.peerDto.PeerDto;
import ru.s21school.entity.Peer;
import ru.s21school.mapper.peerMapper.PeerReadMapper;
import ru.s21school.mapper.peerMapper.PeerCreateEditMapper;
import ru.s21school.repository.PeerRepository;

@Service
@Transactional(readOnly = true)
public class PeerService extends BaseService<Peer, PeerDto, String> {
    public PeerService(PeerRepository repository, PeerReadMapper peerReadMapper, PeerCreateEditMapper peerCreateEditMapper) {
        super(repository, peerReadMapper, peerCreateEditMapper);
    }
}
