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
@Table(name = "verter")
public class Verter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_id")
    @ToString.Exclude
    private Check check;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private State state;

    @Column
    @NotNull
    private LocalTime time;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Verter verter = (Verter) o;
        return getId() != null && Objects.equals(getId(), verter.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
