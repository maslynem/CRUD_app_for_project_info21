package ru.s21school.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import ru.s21school.dto.VerterDto;
import ru.s21school.entity.Verter;
import ru.s21school.mapper.verterMapper.CsvVerterMapper;
import ru.s21school.mapper.verterMapper.VerterCreateEditMapper;
import ru.s21school.mapper.verterMapper.VerterReadMapper;
import ru.s21school.repository.VerterRepository;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Slf4j
@Service
public class VerterService extends BaseService<Verter, VerterDto, Long> {
    protected VerterService(VerterRepository repository, VerterReadMapper checkReadMapper, VerterCreateEditMapper createEditMapper, CsvVerterMapper csvMapper) {
        super(repository, checkReadMapper, createEditMapper, csvMapper);
    }

    public void writeToCsv(Writer writer) {
        List<Verter> list = repository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("id", "check_id", "state", "time");
            for (Verter entity : list) {
                csvPrinter.printRecord(
                        entity.getId(),
                        entity.getCheck().getId(),
                        entity.getState().name(),
                        entity.getTime()
                );
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }
}
