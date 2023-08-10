package ru.s21school.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s21school.entity.Peer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "p2p")
//@TypeDef(typeClass = EnumType.class, defaultForType = State.class)
public class PeerToPeer  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_id")
    @NotNull
    private Check check;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checking_peer")
    @NotNull
    private Peer checkingPeer;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    private State state;

    @Column
    @NotNull
    private LocalTime time;
}
