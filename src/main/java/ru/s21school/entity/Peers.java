package ru.s21school.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "peers")
public class Peers {
    @Id
    @Column
    @NotNull
    private String nickname;

    @Column
    @NotNull
    private LocalDate birthday;
}
