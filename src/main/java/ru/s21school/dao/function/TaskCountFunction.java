package ru.s21school.dao.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.s21school.dao.functionResult.TaskCountResult;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskCountFunction {
    private final JdbcTemplate jdbcTemplate;

    private static final String sqlQuery = "SELECT * FROM ex12()";

    public List<TaskCountResult> execute() {
        log.info("start execute function ex12().");
        try {
            return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(TaskCountResult.class));
        } catch (Exception e) {
            log.warn("FAIL execute function ex12. Message: {}", e.getMessage());
            throw e;
        }
    }
}
