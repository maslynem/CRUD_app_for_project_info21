package ru.s21school.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "tasks")
@Check(constraints = "parent_task != title")
public class Task  {
    @Id
    private String title;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_task")
    @ToString.Exclude
    private Task parentTask;

    @Column(name = "max_xp")
    @NotNull
    private Long maxXp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Task task = (Task) o;
        return getTitle() != null && Objects.equals(getTitle(), task.getTitle());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
