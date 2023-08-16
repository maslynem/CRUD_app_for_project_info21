package ru.s21school.dao.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.s21school.dao.functionResult.TopPeerResult;

@Slf4j
@Component
@RequiredArgsConstructor
public class TopPeerFunction {
    private final JdbcTemplate jdbcTemplate;

    private static final String sqlQuery = "SELECT * FROM ex14()";

    public TopPeerResult execute() {
        log.info("start execute function ex14()");
        try {
            return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(TopPeerResult.class))
                    .stream()
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            log.warn("FAIL execute function ex14. Message: {}", e.getMessage());
            throw e;
        }
    }
}
