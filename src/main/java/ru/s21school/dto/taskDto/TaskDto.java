package ru.s21school.dto.taskDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s21school.entity.Task;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    @NotNull(message = "Title can not be empty")
    @NotEmpty(message = "Title can not be empty")
    private String title;

    private String parentTaskTitle;

    @NotNull()
    @Min(value = 0, message = "MaxXp can not be less than 0")
    private Long maxXp;
}
