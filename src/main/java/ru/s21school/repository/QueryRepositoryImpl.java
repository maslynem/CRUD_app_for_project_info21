package ru.s21school.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QueryRepositoryImpl implements QueryRepository {
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;
    @Override
    public void executeQuery(String query) {
        try {
            log.info("try to execute SELECT query: {}", query);
            jdbcTemplate.execute(query);
            entityManager.createNativeQuery(query).getResultList()
        } catch (RuntimeException e) {
            log.warn("Fail execute query : {}. REASON: {}", query, e.getMessage());
            throw e;
        }
    }
}
