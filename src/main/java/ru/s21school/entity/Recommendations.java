package ru.s21school.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s21school.entity.Peer;

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
    Peer peer;

    @OneToOne
    @JoinColumn(name = "recommended_peer")
    @NotNull
    Peer recommendedPeer;
}
