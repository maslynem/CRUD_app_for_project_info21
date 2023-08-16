package ru.s21school.dao.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.s21school.dao.functionResult.TransferredPointChangeResult;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferredPointsChangeFunctionV1 {
    private final JdbcTemplate jdbcTemplate;

    private static final String sqlQuery = "SELECT * FROM ex04()";

    public List<TransferredPointChangeResult> execute() {
        log.info("start execute function ex04()");
        try {
            return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(TransferredPointChangeResult.class));
        } catch (Exception e) {
            log.warn("FAIL execute function ex04. Message: {}", e.getMessage());
            throw e;
        }
    }
}
