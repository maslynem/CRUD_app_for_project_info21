package ru.s21school.mapper.recommendationMapper;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import ru.s21school.dto.RecommendationDto;
import ru.s21school.mapper.Mapper;

@Component
public class CsvRecommendationMapper implements Mapper<CSVRecord, RecommendationDto> {
    @Override
    public RecommendationDto map(CSVRecord object) {
        RecommendationDto dto = new RecommendationDto();
        dto.setId(Long.valueOf(object.get("id")));
        dto.setPeerNickname(object.get("peer"));
        dto.setRecommendedPeerNickname(object.get("recommended_peer"));
        return dto;
    }
}
