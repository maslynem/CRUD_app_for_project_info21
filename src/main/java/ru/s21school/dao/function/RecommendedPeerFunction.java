package ru.s21school.dao.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.s21school.dao.functionResult.RecommendedPeerResult;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendedPeerFunction {
    private final JdbcTemplate jdbcTemplate;

    private static final String sqlQuery = "SELECT * FROM ex08()";

    public List<RecommendedPeerResult> execute() {
        log.info("start execute function ex08()");
        try {
            return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(RecommendedPeerResult.class));
        } catch (Exception e) {
            log.warn("FAIL execute function ex08. Message: {}", e.getMessage());
            throw e;
        }
    }
}
