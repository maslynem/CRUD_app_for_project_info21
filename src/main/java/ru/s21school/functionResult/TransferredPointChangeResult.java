package ru.s21school.functionResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferredPointChangeResult {
    private String peer;
    private Long pointsChange;
}
