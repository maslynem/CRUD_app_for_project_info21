package ru.s21school.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import ru.s21school.dto.CheckDto;
import ru.s21school.entity.Check;
import ru.s21school.mapper.checkMapper.CheckCreateEditMapper;
import ru.s21school.mapper.checkMapper.CheckReadMapper;
import ru.s21school.mapper.checkMapper.CsvToCheckMapper;
import ru.s21school.repository.CheckRepository;

import java.io.*;
import java.util.List;

@Slf4j
@Service
public class CheckService extends BaseService<Check, CheckDto, Long> {
    protected CheckService(CheckRepository repository, CheckReadMapper checkReadMapper, CheckCreateEditMapper createEditMapper, CsvToCheckMapper csvMapper) {
        super(repository, checkReadMapper, createEditMapper, csvMapper);
    }

    public void writeToCsv(Writer writer) {
        List<Check> checks = repository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("id", "peer", "task", "date");
            for (Check check : checks) {
                csvPrinter.printRecord(
                        check.getId(),
                        check.getPeer().getNickname(),
                        check.getTask().getTitle(),
                        check.getDate());
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }
}
