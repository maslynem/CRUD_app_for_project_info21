package ru.s21school.dto.operationParametersDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddP2pCheckDto {
    @NotBlank(message = "Can not be empty")

    private String checkingPeer;

    @NotBlank(message = "Can not be empty")
    private String checkedPeer;

    @NotBlank(message = "Can not be empty")
    private String taskTitle;

    @NotBlank(message = "Can not be empty")
    private String state;

    @NotNull(message = "Can not be empty")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime checkTime;
}
