package ru.s21school.mapper.recommendationMapper;

import org.springframework.stereotype.Component;
import ru.s21school.dto.RecommendationDto;
import ru.s21school.entity.Recommendation;
import ru.s21school.mapper.Mapper;

@Component
public class RecommendationReadMapper implements Mapper<Recommendation, RecommendationDto> {
    @Override
    public RecommendationDto map(Recommendation object) {
        return new RecommendationDto(
                object.getId(),
                object.getPeer().getNickname(),
                object.getRecommendedPeer().getNickname()
        );
    }
}
