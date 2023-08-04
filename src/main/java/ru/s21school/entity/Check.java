package ru.s21school.entity;

import lombok.*;
import ru.s21school.entity.Peer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "checks")
public class Check {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "peer")
    @NotNull
    private Peer peer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task")
    @NotNull
    private Task task;

    @Column(name = "date")
    @NotNull
    private LocalDate date;
}
