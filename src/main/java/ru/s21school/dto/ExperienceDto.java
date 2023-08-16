package ru.s21school.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExperienceDto {
    private Long id;

    @NotNull(message = "Can not be empty")
    private Long checkId;

    @Positive(message = "Must be greater than 0")
    @NotNull(message = "Can not be empty")
    private Long xpAmount;
}
