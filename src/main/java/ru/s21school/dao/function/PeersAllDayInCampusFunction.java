package ru.s21school.dao.function;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PeersAllDayInCampusFunction {
    private final JdbcTemplate jdbcTemplate;

    private static final String sqlQuery = "SELECT * FROM ex03(?)";

    public List<String> execute(LocalDate day) {
        log.info("start execute function ex02(). Date: {}", day);
        try {
            return jdbcTemplate.queryForList(sqlQuery, String.class, day);
        } catch (Exception e) {
            log.warn("{}", e.getMessage());
            throw e;
        }
    }
}
