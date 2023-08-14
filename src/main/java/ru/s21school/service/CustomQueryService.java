package ru.s21school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.s21school.mapper.customQueryResultMapper.CustomQueryResultMapper;
import ru.s21school.repository.QueryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomQueryService {
    private final QueryRepository queryRepository;
    private final CustomQueryResultMapper customQueryResultMapper;

    public List<String> executeSelect(String query) {
        List list = queryRepository.executeSelectQuery(query);
        return customQueryResultMapper.map(list);
    }

    public int executeUpdate(String query) {
        return queryRepository.executeUpdateQuery(query);
    }
}
