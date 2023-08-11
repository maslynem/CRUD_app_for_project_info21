package ru.s21school.dto.checkDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CheckDto {
    private Long id;

    @NotBlank(message = "Nickname can not be empty")
    private String peerNickname;

    @NotBlank(message = "Title can not be empty")
    private String taskTitle;

    @NotNull(message = "Date can not be empty")
    @PastOrPresent(message = "Must contain past or present date")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate date;
}
