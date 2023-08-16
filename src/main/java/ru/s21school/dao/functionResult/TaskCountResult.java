package ru.s21school.dao.functionResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCountResult {
    private String task;
    private Integer prevCount;
}
