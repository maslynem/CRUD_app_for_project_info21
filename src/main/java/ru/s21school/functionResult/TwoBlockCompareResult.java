package ru.s21school.functionResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwoBlockCompareResult {
    private Long startedBlock1;
    private Long startedBlock2;
    private Long startedBothBlocks;
    private Long didntStartAnyBlock;
}
