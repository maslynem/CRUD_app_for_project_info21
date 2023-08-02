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
@Table(name = "Recommendations", uniqueConstraints = @UniqueConstraint(columnNames = {"peer", "recommended_peer"}))
public class Recommendations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "peer")
    @NotNull
    Peers peer;

    @OneToOne
    @JoinColumn(name = "recommended_peer")
    @NotNull
    Peers recommendedPeer;
}
