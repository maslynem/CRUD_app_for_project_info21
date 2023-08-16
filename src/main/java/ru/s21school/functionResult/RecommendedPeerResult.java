package ru.s21school.functionResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedPeerResult {
    private String peer;
    private String recommendedPeer;
}
