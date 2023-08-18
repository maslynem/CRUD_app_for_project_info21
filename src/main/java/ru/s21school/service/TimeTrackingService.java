package ru.s21school.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import ru.s21school.dto.TimeTrackingDto;
import ru.s21school.entity.TimeTracking;
import ru.s21school.mapper.timeTrackingMapper.CsvTimeTrackingMapper;
import ru.s21school.mapper.timeTrackingMapper.TImeTrackingSaveEditMapper;
import ru.s21school.mapper.timeTrackingMapper.TimeTrackingReadMapper;
import ru.s21school.repository.TimeTrackingRepository;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Slf4j
@Service
public class TimeTrackingService extends BaseService<TimeTracking, TimeTrackingDto, Long> {
    protected TimeTrackingService(TimeTrackingRepository repository, TimeTrackingReadMapper readMapper, TImeTrackingSaveEditMapper saveEditMapper, CsvTimeTrackingMapper csvMapper) {
        super(repository, readMapper, saveEditMapper, csvMapper);
    }


    public void writeToCsv(Writer writer) {
        List<TimeTracking> list = repository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("id", "peer", "date", "time", "state");
            for (TimeTracking entity : list) {
                csvPrinter.printRecord(
                        entity.getId(),
                        entity.getPeer().getNickname(),
                        entity.getDate(),
                        entity.getTime(),
                        entity.getState()
                );
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }
}
