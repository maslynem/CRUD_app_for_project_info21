package ru.s21school.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskDto {

    @NotBlank(message = "Title can not be empty")
    @NotEmpty(message = "Title can not be empty")
    private String title;

    private String parentTaskTitle;

    @Digits(message = "MaxXp should be a number", integer = 18, fraction = 0)
    @NotNull(message = "MaxXp can not be less than 0")
    @Positive(message = "MaxXp can not be less than 0")
    private Long maxXp;
}
