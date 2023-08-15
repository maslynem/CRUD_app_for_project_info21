package ru.s21school.dao.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferredPointsHumanReadFunction {
    private final JdbcTemplate jdbcTemplate;

    private static final String sqlQuery = "SELECT * FROM ex01()";

    public List<Map<String, Object>> execute() {
        log.info("start execute function ex01()");
        try {
            return jdbcTemplate.queryForList(sqlQuery);
        } catch (Exception e) {
            log.warn("{}", e.getMessage());
            throw e;
        }
    }
}
