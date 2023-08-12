package ru.s21school.service;

import org.springframework.stereotype.Service;
import ru.s21school.dto.RecommendationDto;
import ru.s21school.entity.Recommendation;
import ru.s21school.mapper.recommendationMapper.RecommendationCreateEditMapper;
import ru.s21school.mapper.recommendationMapper.RecommendationReadMapper;
import ru.s21school.repository.RecommendationRepository;

@Service
public class RecommendationService extends BaseService<Recommendation, RecommendationDto, Long> {
    protected RecommendationService(RecommendationRepository repository, RecommendationReadMapper readMapper, RecommendationCreateEditMapper createEditMapper) {
        super(repository, readMapper, createEditMapper);
    }
}
