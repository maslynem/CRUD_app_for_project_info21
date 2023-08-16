package ru.s21school.dao.function;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompletedTwoTaskWithoutThirdFunction {
    private final JdbcTemplate jdbcTemplate;

    private static final String sqlQuery = "SELECT * FROM ex11(?, ?, ?)";

    public List<String> execute(String firstTask, String secondTask, String thirdTask) {
        log.info("start execute function ex11(). firstTask: {}. SecondTask: {}. ThirdTask: {}", firstTask, secondTask, thirdTask);
        try {
            return jdbcTemplate.queryForList(sqlQuery, String.class, firstTask, secondTask, thirdTask);
        } catch (Exception e) {
            log.warn("FAIL execute function ex11. Message: {}", e.getMessage());
            throw e;
        }
    }
}
