package ru.s21school.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FriendDto {
    private Long id;

    @NotBlank(message = "Can not be empty")
    private String peerOneNickname;

    @NotBlank(message = "Can not be empty")
    private String peerTwoNickname;
}
