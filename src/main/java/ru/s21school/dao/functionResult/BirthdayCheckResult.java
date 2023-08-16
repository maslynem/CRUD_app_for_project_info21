package ru.s21school.dao.functionResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BirthdayCheckResult {
    private Integer successfulChecks;
    private Integer unsuccessfulChecks;
}
