package ru.s21school.dto.peerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class PeerDto {
    @NotNull(message = "Nickname can not be empty")
    @NotEmpty(message = "Nickname can not be empty")
    private String nickname;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "Birthday can not be empty")
    private LocalDate birthday;
}
