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
@Table(name = "transferred_points")
public class TransferredPoints  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checking_peer")
    @NotNull
    private Peer checkingPeer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checked_peer")
    @NotNull
    private Peer checkedPeer;

    @Column(name = "points_amount")
    @NotNull
    Long pointsAmount;
}
