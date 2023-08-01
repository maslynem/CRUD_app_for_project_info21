package ru.s21school.peerDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Peer {
    private int id;
    private String name;
    private String surname;
    private String email;

}
