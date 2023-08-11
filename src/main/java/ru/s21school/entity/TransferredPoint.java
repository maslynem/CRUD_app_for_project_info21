package ru.s21school.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transferred_points")
public class TransferredPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checking_peer")
    @NotNull
    @ToString.Exclude
    private Peer checkingPeer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checked_peer")
    @NotNull
    @ToString.Exclude
    private Peer checkedPeer;

    @Column(name = "points_amount")
    @NotNull
    private Long pointsAmount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TransferredPoint that = (TransferredPoint) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
