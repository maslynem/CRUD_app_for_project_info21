package ru.s21school.entity;

import lombok.*;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tasks")
@ToString(exclude = "parentTask")
@Check(constraints = "parent_task != title")
public class Tasks {
    @Id
    private String title;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_task")
    private Tasks parentTask;

    @Column(name = "max_xp")
    @NotNull
    private Long maxXp;
}
