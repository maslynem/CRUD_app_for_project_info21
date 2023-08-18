package ru.s21school.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import ru.s21school.dto.PeerDto;
import ru.s21school.entity.Peer;
import ru.s21school.mapper.peerMapper.CsvToPeerMapper;
import ru.s21school.mapper.peerMapper.PeerReadMapper;
import ru.s21school.mapper.peerMapper.PeerCreateEditMapper;
import ru.s21school.repository.PeerRepository;

import java.io.*;
import java.util.List;

@Service
@Slf4j
public class PeerService extends BaseService<Peer, PeerDto, String> {

    public PeerService(PeerRepository repository, PeerReadMapper peerReadMapper, PeerCreateEditMapper peerCreateEditMapper, CsvToPeerMapper csvMapper) {
        super(repository, peerReadMapper, peerCreateEditMapper, csvMapper);
    }

    public void writeToCsv(Writer writer) {
        List<Peer> peers = repository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("Nickname", "Birthday");
            for (Peer peer : peers) {
                csvPrinter.printRecord(peer.getNickname(), peer.getBirthday());
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }
}
