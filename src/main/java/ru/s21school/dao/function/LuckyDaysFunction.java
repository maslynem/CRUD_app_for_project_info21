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
public class LuckyDaysFunction {
    private final JdbcTemplate jdbcTemplate;

    private static final String sqlQuery = "SELECT * FROM ex13(?)";

    public List<LocalDate> execute(Integer n) {
        log.info("start execute function ex13(). N: {}", n);
        try {
            return jdbcTemplate.queryForList(sqlQuery, LocalDate.class, n);
        } catch (Exception e) {
            log.warn("FAIL execute function ex13. Message: {}", e.getMessage());
            throw e;
        }
    }
}
