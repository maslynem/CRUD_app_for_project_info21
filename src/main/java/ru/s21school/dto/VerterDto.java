package ru.s21school.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import ru.s21school.entity.State;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VerterDto {
    private Long id;

    @NotNull(message = "Can not be empty")
    private Long checkId;

    @NotNull(message = "Can not be empty")
    private State state;

    @NotNull(message = "Can not be empty")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime time;

}
