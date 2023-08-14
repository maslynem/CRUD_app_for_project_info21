package ru.s21school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.s21school.repository.QueryRepositoryImpl;

@Service
@RequiredArgsConstructor
public class CustomQueryService {
    private final QueryRepositoryImpl queryRepository;

    public void execute(String query) {
        queryRepository.executeQuery(query);
    }
}
