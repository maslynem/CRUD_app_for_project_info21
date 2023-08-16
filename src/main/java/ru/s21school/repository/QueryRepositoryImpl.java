package ru.s21school.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QueryRepositoryImpl implements QueryRepository {
    private final EntityManager entityManager;

    @Override
    public List executeSelectQuery(String query) {
        try {
            log.info("try to execute SELECT query: {}", query);
            return entityManager.createNativeQuery(query).getResultList();
        } catch (RuntimeException e) {
            log.warn("Fail execute query : {}. REASON: {}", query, e.getMessage());
            throw e;
        }
    }

    @Override
    public int executeUpdateQuery(String query) {
        try {
            log.info("try to execute UPDATE query: {}", query);
            return entityManager.createNativeQuery(query).executeUpdate();
        } catch (RuntimeException e) {
            log.warn("Fail execute query : {}. REASON: {}", query, e.getMessage());
            throw e;
        }

    }
}
