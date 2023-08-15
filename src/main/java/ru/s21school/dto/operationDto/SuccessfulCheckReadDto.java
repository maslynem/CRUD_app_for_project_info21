package ru.s21school.dto.operationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessfulCheckReadDto {
    private String peer;
    private String task;
    private Long xp;
}
