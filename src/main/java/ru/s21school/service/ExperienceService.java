package ru.s21school.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import ru.s21school.dto.ExperienceDto;
import ru.s21school.entity.Experience;
import ru.s21school.mapper.experienceMapper.CsvExperienceMapper;
import ru.s21school.mapper.experienceMapper.ExperienceCreateEditMapper;
import ru.s21school.mapper.experienceMapper.ExperienceReadMapper;
import ru.s21school.repository.ExperienceRepository;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Slf4j
@Service
public class ExperienceService extends BaseService<Experience, ExperienceDto, Long> {
    protected ExperienceService(ExperienceRepository repository, ExperienceReadMapper readMapper, ExperienceCreateEditMapper createEditMapper, CsvExperienceMapper csvMapper) {
        super(repository, readMapper, createEditMapper, csvMapper);
    }

    public void writeToCsv(Writer writer) {
        List<Experience> list = repository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("id", "check_id", "xp_amount");
            for (Experience entity : list) {
                csvPrinter.printRecord(
                        entity.getId(),
                        entity.getCheck().getId(),
                        entity.getXpAmount()
                );
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }
}
