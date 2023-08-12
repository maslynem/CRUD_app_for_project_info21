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
public class RecommendationDto {
    private Long id;

    @NotBlank(message = "Can not be empty")
    private String peerNickname;

    @NotBlank(message = "Can not be empty")
    private String recommendedPeerNickname;

}
