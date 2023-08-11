package ru.s21school.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "peers")
public class Peer {
    @Id
    @Column
    @NotNull
    private String nickname;

    @Column
    @NotNull
    private LocalDate birthday;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Peer peer = (Peer) o;
        return getNickname() != null && Objects.equals(getNickname(), peer.getNickname());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
