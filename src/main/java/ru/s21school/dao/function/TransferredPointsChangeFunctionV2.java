package ru.s21school.dao.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.s21school.functionResult.TransferredPointChangeResult;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferredPointsChangeFunctionV2 {
    private final JdbcTemplate jdbcTemplate;

    private static final String sqlQuery = "SELECT * FROM ex05()";

    public List<TransferredPointChangeResult> execute() {
        log.info("start execute function ex05()");
        try {
            return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(TransferredPointChangeResult.class));
        } catch (Exception e) {
            log.warn("FAIL execute function ex05. Message: {}", e.getMessage());
            throw e;
        }
    }
}
