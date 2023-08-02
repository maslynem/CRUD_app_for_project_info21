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
@Table(name = "transferredPoints")
public class TransferredPoints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checking_peer")
    @NotNull
    private Peers checkingPeer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checked_peer")
    @NotNull
    private Peers checkedPeer;

    @Column(name = "points_amount")
    @NotNull
    Long pointsAmount;
}
