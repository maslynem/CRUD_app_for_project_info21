package ru.s21school.dto;

import lombok.Value;

import java.time.LocalDate;

@Value
public class PeerDto {
    String nickname;
    LocalDate birthday;
}
