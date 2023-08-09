package ru.s21school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.s21school.dto.PagePeerDto;
import ru.s21school.dto.PeerDto;
import ru.s21school.entity.Peer;
import ru.s21school.mapper.PeerReadMapper;
import ru.s21school.mapper.PeerSaveMapper;
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
    private final PeerSaveMapper peerSaveMapper;

    public List<PeerDto> findAll() {
        return peerRepository.findAll().stream()
                .map(peerReadMapper::map)
                .collect(Collectors.toList());
    }

    public PagePeerDto findPeersWithPaginationAndSorting(int page, int pageSize, String sortField, String sortDirection ) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<Peer> all = peerRepository.findAll(pageable);
        return new PagePeerDto(all.getContent(), all.getTotalPages(), all.getTotalElements(), peerReadMapper);
    }

    public PeerDto findByNickname(String nickname) {
        Optional<Peer> byId = peerRepository.findById(nickname);
        return byId.map(peerReadMapper::map).orElse(null);
    }

    @Transactional
    public void update(PeerDto peerUpdate) {
        peerRepository.save(peerSaveMapper.map(peerUpdate));
    }

    @Transactional
    public void save(PeerDto peer) {
        peerRepository.save(peerSaveMapper.map(peer));
    }
}
