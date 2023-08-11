package ru.s21school.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
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
@Table(name = "checks")
public class Check {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "peer")
    @NotNull
    @ToString.Exclude
    private Peer peer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task")
    @NotNull
    @ToString.Exclude
    private Task task;

    @Column(name = "date")
    @NotNull
    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Check check = (Check) o;
        return getId() != null && Objects.equals(getId(), check.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
