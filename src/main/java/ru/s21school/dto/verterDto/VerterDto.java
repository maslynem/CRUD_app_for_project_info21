package ru.s21school.dto.verterDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s21school.entity.State;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerterDto {
    Long id;

    @NotNull(message = "Can not be empty")
    Long checkId;

    @NotNull(message = "Can not be empty")
    State state;

    @NotNull(message = "Can not be empty")
    LocalTime time;
}
