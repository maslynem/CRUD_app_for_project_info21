package ru.s21school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.s21school.dto.peerDto.PagePeerDto;
import ru.s21school.dto.peerDto.PeerDto;
import ru.s21school.entity.Peer;
import ru.s21school.mapper.PeerReadMapper;
import ru.s21school.mapper.PeerCreateEditMapper;
import ru.s21school.repository.PeerRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PeerService {
    private final PeerRepository peerRepository;
    private final PeerReadMapper peerReadMapper;
    private final PeerCreateEditMapper peerCreateEditMapper;

    public List<PeerDto> findAll() {
        return peerRepository.findAll().stream()
                .map(peerReadMapper::map)
                .collect(Collectors.toList());
    }

    public PagePeerDto findPeersWithPaginationAndSorting(int page, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<Peer> all = peerRepository.findAll(pageable);
        return new PagePeerDto(all.getContent(), all.getTotalPages(), all.getTotalElements(), peerReadMapper);
    }

    public Optional<PeerDto> findByNickname(String nickname) {
        return peerRepository.findById(nickname).map(peerReadMapper::map);
    }

    @Transactional
    public PeerDto save(PeerDto peerDto) {
        return Optional.of(peerDto)
                .map(peerCreateEditMapper::map)
                .map(peerRepository::save)
                .map(peerReadMapper::map)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public Optional<PeerDto> update(String nickname, PeerDto peerDto) {
        return peerRepository.findById(nickname)
                .map(entity -> peerCreateEditMapper.map(peerDto, entity))
                .map(peerRepository::saveAndFlush)
                .map(peerReadMapper::map);
    }

    @Transactional
    public boolean delete(String name) {
        return peerRepository.findById(name)
                .map(entity -> {
                    peerRepository.delete(entity);
                    return true;
                }).orElse(false);
    }
}
