package ru.s21school.repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QueryRepository {
    @Transactional(readOnly = true)
    List executeSelectQuery(String query);

    @Transactional
    int executeUpdateQuery(String query);


}
