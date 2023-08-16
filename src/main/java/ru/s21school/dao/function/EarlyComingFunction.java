package ru.s21school.dao.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EarlyComingFunction {
    private final JdbcTemplate jdbcTemplate;

    private static final String sqlQuery = "SELECT * FROM ex15(?, ?)";

    public List<String> execute(LocalTime time, Integer n) {
        log.info("start execute function ex15(). Time: {}. N: {}", time, n);
        try {
            return jdbcTemplate.queryForList(sqlQuery, String.class, time, n);
        } catch (Exception e) {
            log.warn("FAIL execute function ex15. Message: {}", e.getMessage());
            throw e;
        }
    }
}
