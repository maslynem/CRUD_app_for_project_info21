package ru.s21school.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import ru.s21school.dto.RecommendationDto;
import ru.s21school.entity.Recommendation;
import ru.s21school.mapper.recommendationMapper.CsvRecommendationMapper;
import ru.s21school.mapper.recommendationMapper.RecommendationCreateEditMapper;
import ru.s21school.mapper.recommendationMapper.RecommendationReadMapper;
import ru.s21school.repository.RecommendationRepository;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Slf4j
@Service
public class RecommendationService extends BaseService<Recommendation, RecommendationDto, Long> {
    protected RecommendationService(RecommendationRepository repository, RecommendationReadMapper readMapper, RecommendationCreateEditMapper createEditMapper, CsvRecommendationMapper csvMapper) {
        super(repository, readMapper, createEditMapper, csvMapper);
    }


    public void writeToCsv(Writer writer) {
        List<Recommendation> list = repository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("id", "peer", "recommended_peer");
            for (Recommendation entity : list) {
                csvPrinter.printRecord(
                        entity.getId(),
                        entity.getPeer().getNickname(),
                        entity.getRecommendedPeer().getNickname()
                );
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }
}
