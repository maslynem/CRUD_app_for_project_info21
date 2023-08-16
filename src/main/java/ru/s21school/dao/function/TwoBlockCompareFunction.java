package ru.s21school.dao.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.s21school.dao.functionResult.TwoBlockCompareResult;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TwoBlockCompareFunction {

    private final JdbcTemplate jdbcTemplate;

    private static final String sqlQuery = "SELECT * FROM ex09(?,?)";

    public List<TwoBlockCompareResult> execute(String firstBlock, String secondBlock) {
        log.info("start execute function ex09() firstBlock: {}. secondBlock: {}", firstBlock, secondBlock);
        try {
            return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(TwoBlockCompareResult.class), firstBlock, secondBlock);
        } catch (Exception e) {
            log.warn("FAIL execute function ex09. Message: {}", e.getMessage());
            throw e;
        }
    }

}
