package ru.s21school.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransferredPointDto {

    private  Long id;

    @NotBlank(message = "Can not be empty")
    private  String checkingPeerNickname;

    @NotBlank(message = "Can not be empty")
    private  String checkedPeerNickname;

    @NotNull(message = "Can not be empty")
    private  Long pointsAmount;
}
