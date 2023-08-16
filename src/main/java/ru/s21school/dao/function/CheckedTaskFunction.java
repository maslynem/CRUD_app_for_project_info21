package ru.s21school.dao.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.s21school.functionResult.CheckedTaskResult;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckedTaskFunction {
    private final JdbcTemplate jdbcTemplate;

    private static final String sqlQuery = "SELECT * FROM ex06()";

    public List<CheckedTaskResult> execute() {
        log.info("start execute function ex05()");
        try {
            return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(CheckedTaskResult.class));
        } catch (Exception e) {
            log.warn("FAIL execute function ex05. Message: {}", e.getMessage());
            throw e;
        }
    }
}
