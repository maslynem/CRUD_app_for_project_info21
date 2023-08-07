package ru.s21school.dto;

import lombok.AllArgsConstructor;
import lombok.Value;
import ru.s21school.entity.Peer;
import ru.s21school.mapper.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Value
@AllArgsConstructor
public class PagePeerDto {
    List<PeerDto> peersDto;
    long totalPages;
    long totalElements;

    public PagePeerDto(List<Peer> peers, long totalPages, long totalElements , Mapper<Peer, PeerDto> peerReadMapper) {
        this.peersDto = peers.stream()
                .map(peerReadMapper::map)
                .collect(Collectors.toList());
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}
