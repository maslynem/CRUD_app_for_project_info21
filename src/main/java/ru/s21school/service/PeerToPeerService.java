package ru.s21school.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import ru.s21school.dto.PeerToPeerDto;
import ru.s21school.entity.PeerToPeer;
import ru.s21school.mapper.peerToPeerMapper.CsvPeerToPeerMapper;
import ru.s21school.mapper.peerToPeerMapper.PeerToPeerCreateEditMapper;
import ru.s21school.mapper.peerToPeerMapper.PeerToPeerReadMapper;
import ru.s21school.repository.PeerToPeerRepository;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Slf4j
@Service
public class PeerToPeerService extends BaseService<PeerToPeer, PeerToPeerDto, Long> {

    protected PeerToPeerService(PeerToPeerRepository repository, PeerToPeerReadMapper readMapper, PeerToPeerCreateEditMapper createEditMapper, CsvPeerToPeerMapper csvMapper) {
        super(repository, readMapper, createEditMapper, csvMapper);
    }


    public void writeToCsv(Writer writer) {
        List<PeerToPeer> list = repository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("id", "check_id", "checking_peer", "state", "time");
            for (PeerToPeer entity : list) {
                csvPrinter.printRecord(
                        entity.getId(),
                        entity.getCheck().getId(),
                        entity.getCheckingPeer().getNickname(),
                        entity.getState().name(),
                        entity.getTime()
                );
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }

}
