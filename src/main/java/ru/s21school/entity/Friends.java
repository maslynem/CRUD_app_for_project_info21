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
@Table(name = "Friends", uniqueConstraints = @UniqueConstraint(columnNames = {"peer1", "peer2"}))
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "peer1")
    @NotNull
    private Peer peer1;

    @OneToOne
    @JoinColumn(name = "peer2")
    @NotNull
    private Peer peer2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Friends friends = (Friends) o;
        return getId() != null && Objects.equals(getId(), friends.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
