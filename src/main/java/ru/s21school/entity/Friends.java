package ru.s21school.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Friends", uniqueConstraints = @UniqueConstraint(columnNames = {"peer1", "peer2"}))
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "peer1")
    @NotNull
    Peers peer1;

    @OneToOne
    @JoinColumn(name = "peer2")
    @NotNull
    Peers peer2;

}
