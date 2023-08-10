package ru.s21school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.s21school.dto.peerDto.PeerDto;
import ru.s21school.entity.Peer;
import ru.s21school.mapper.Mapper;
import ru.s21school.mapper.peerMapper.PeerReadMapper;
import ru.s21school.mapper.peerMapper.PeerCreateEditMapper;
import ru.s21school.repository.PeerRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PeerService extends BaseService<Peer, PeerDto, String> {
    public PeerService(PeerRepository repository, PeerReadMapper peerReadMapper, PeerCreateEditMapper peerCreateEditMapper) {
        super(repository, peerReadMapper, peerCreateEditMapper);
    }
}
