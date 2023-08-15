package ru.s21school.dao.procedure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddVerterProcedure {
    private final JdbcTemplate jdbcTemplate;

    private static final String sqlQuery = "CALL add_verter_check(?, ?, ?, ?)";

    public void execute( String checkedPeer, String taskTitle, String state, LocalTime checkTime) {
        log.info("start execute procedure add_verter_check");
        try {
            jdbcTemplate.update(sqlQuery, checkedPeer, taskTitle, state, checkTime);
            log.info("procedure add_verter_check was executed");
        } catch (Exception e) {
            log.warn("{}", e.getMessage());
            throw e;
        }
    }
}
