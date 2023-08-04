package ru.s21school.dto;

import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Value
public class PeerDto {
    String nickname;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    LocalDate birthday;
}
