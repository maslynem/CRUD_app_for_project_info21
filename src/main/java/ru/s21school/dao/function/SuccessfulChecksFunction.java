package ru.s21school.dao.function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.s21school.dto.operationDto.SuccessfulCheckReadDto;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SuccessfulChecksFunction {
    private final JdbcTemplate jdbcTemplate;

    private static final String sqlQuery = "SELECT * FROM ex02()";

    public List<SuccessfulCheckReadDto> execute() {
        log.info("start execute function ex02()");
        try {
            return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(SuccessfulCheckReadDto.class));
        } catch (Exception e) {
            log.warn("{}", e.getMessage());
            throw e;
        }
    }
}
