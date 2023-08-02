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
@Table(name = "checks")
public class Checks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "peer")
    @NotNull
    private Peers peer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task")
    @NotNull
    private Tasks task;

    @Column(name = "date")
    @NotNull
    private LocalDate date;
}
