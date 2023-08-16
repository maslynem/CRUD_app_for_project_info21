package ru.s21school.dao.functionResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessfulCheckResult {
    private String peer;
    private String task;
    private Long xp;
}
