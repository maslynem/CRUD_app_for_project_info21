package ru.s21school.dao.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LeavingFromCampusFunction {
    private final JdbcTemplate jdbcTemplate;

    private static final String sqlQuery = "SELECT * FROM ex16(?, ?)";

    public List<String> execute(Integer n, Integer m) {
        log.info("start execute function ex16(). N: {}. M: {}", n, m);
        try {
            return jdbcTemplate.queryForList(sqlQuery, String.class, n, m);
        } catch (Exception e) {
            log.warn("FAIL execute function ex16. Message: {}", e.getMessage());
            throw e;
        }
    }
}
