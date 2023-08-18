package ru.s21school.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import ru.s21school.dto.TransferredPointDto;
import ru.s21school.entity.TransferredPoint;
import ru.s21school.mapper.transferredPointMapper.CsvTransferredPointMapper;
import ru.s21school.mapper.transferredPointMapper.TransferredPointCreateEditMapping;
import ru.s21school.mapper.transferredPointMapper.TransferredPointReadMapping;
import ru.s21school.repository.TransferredPointRepository;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Slf4j
@Service
public class TransferredPointService extends BaseService<TransferredPoint, TransferredPointDto, Long> {
    protected TransferredPointService(TransferredPointRepository repository, TransferredPointReadMapping readMapping, TransferredPointCreateEditMapping createEditMapping, CsvTransferredPointMapper csvMapper) {
        super(repository, readMapping, createEditMapping, csvMapper);
    }

    public void writeToCsv(Writer writer) {
        List<TransferredPoint> list = repository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("id", "checking_peer", "checked_peer", "points_amount");
            for (TransferredPoint entity : list) {
                csvPrinter.printRecord(
                        entity.getId(),
                        entity.getCheckingPeer().getNickname(),
                        entity.getCheckedPeer().getNickname(),
                        entity.getPointsAmount()
                );
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }
}
