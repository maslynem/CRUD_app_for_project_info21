package ru.s21school.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import ru.s21school.entity.CheckState;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PeerToPeerDto {

    private Long id;

    @NotNull(message = "can not be empty")
    private Long checkId;

    @NotBlank(message = "can not be empty")
    private String checkingPeerNickname;

    @NotNull(message = "can not be empty")
    private CheckState checkState;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull(message = "can not be empty")
    private LocalTime time;
}
