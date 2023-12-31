package ru.s21school.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "p2p")
public class PeerToPeer  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_id")
    @NotNull
    @ToString.Exclude
    private Check check;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checking_peer")
    @NotNull
    @ToString.Exclude
    private Peer checkingPeer;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    private CheckState state;

    @Column
    @NotNull
    private LocalTime time;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PeerToPeer that = (PeerToPeer) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
