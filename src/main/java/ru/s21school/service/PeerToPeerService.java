package ru.s21school.service;

import org.springframework.stereotype.Service;
import ru.s21school.dto.PeerToPeerDto;
import ru.s21school.dto.operationParametersDto.AddP2pCheckDto;
import ru.s21school.entity.PeerToPeer;
import ru.s21school.mapper.peerToPeerMapper.PeerToPeerCreateEditMapper;
import ru.s21school.mapper.peerToPeerMapper.PeerToPeerReadMapper;
import ru.s21school.repository.PeerToPeerRepository;

@Service
public class PeerToPeerService extends BaseService<PeerToPeer, PeerToPeerDto, Long> {
    private PeerToPeerRepository repository;

    protected PeerToPeerService(PeerToPeerRepository repository, PeerToPeerReadMapper readMapper, PeerToPeerCreateEditMapper createEditMapper) {
        super(repository, readMapper, createEditMapper);
        this.repository = repository;
    }

    public void executeAddP2pCheck(AddP2pCheckDto dto) {
        repository.ADD_P2P_CHECK(dto.getCheckingPeer(), dto.getCheckedPeer(), dto.getTaskTitle(), dto.getState(), dto.getCheckTime());
    }

}
